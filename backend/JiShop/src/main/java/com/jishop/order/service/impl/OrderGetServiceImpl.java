package com.jishop.order.service.impl;

import com.jishop.cart.dto.CartDetailResponse;
import com.jishop.cart.dto.CartResponse;
import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.member.domain.User;
import com.jishop.order.domain.Order;
import com.jishop.order.domain.OrderDetail;
import com.jishop.order.domain.OrderStatus;
import com.jishop.order.dto.*;
import com.jishop.order.repository.OrderDetailRepository;
import com.jishop.order.repository.OrderRepository;
import com.jishop.order.service.OrderGetService;
import com.jishop.order.service.OrderUtilService;
import com.jishop.review.repository.ReviewRepository;
import com.jishop.saleproduct.domain.SaleProduct;
import com.jishop.saleproduct.repository.SaleProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderGetServiceImpl implements OrderGetService {

    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;
    private final OrderUtilService orderUtilService;
    private final SaleProductRepository saleProductRepository;
    private final OrderDetailRepository orderDetailRepository;

    //비회원 주문 상세 조회
    @Override
    public OrderDetailPageResponse getOrder(String orderNumber, String phone){
        Order order = orderRepository.findByOrderNumberAndPhone(orderNumber,phone)
                .orElseThrow(() -> new DomainException(ErrorType.ORDER_NOT_FOUND));

        return createOrderDetailPageResponse(order, null);
    }

    //비회원 주문 취소 상세 조회
    @Override
    public OrderCancelResponse getCancelPage(String orderNumber, String phone){
        Order order = orderRepository.findByOrderNumberAndPhone(orderNumber,phone)
                .orElseThrow(() -> new DomainException(ErrorType.ORDER_NOT_FOUND));

        OrderDetailPageResponse page = createOrderDetailPageResponse(order, null);

        return new OrderCancelResponse(order.getUpdatedAt(), page);
    }

    //비회원 주문서로 넘어가는 API
    @Override
    public CartResponse getCheckout(List<OrderDetailRequest> orderDetailRequests){
        return getCheckout(null, orderDetailRequests);
    }

    // 회원 주문 상세 조회
    @Override
    public OrderDetailPageResponse getOrder(User user, Long orderId) {
        Order order = orderRepository.findByIdWithDetailsAndProducts(user.getId(), orderId)
                .orElseThrow(() -> new DomainException(ErrorType.ORDER_NOT_FOUND));

        return createOrderDetailPageResponse(order, user);
    }

    // 회원 주문 전체 조회 페이징 처리
    @Override
    public Page<OrderResponse> getPaginatedOrders(User user, String period, int page, int size) {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime startDate = switch (period) {
            case "1month" -> today.minusMonths(1);
            case "6months" -> today.minusMonths(6);
            default ->
                // LocalDateTime.MIN 대신 충분히 과거의 날짜를 사용
                    LocalDateTime.of(1970, 1, 1, 0, 0, 0);
        };

        // 첫번째 쿼리: 페이징 된 ID 목록 가져오기
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Long> orderIdsPage = orderRepository.findOrderIdsByPeriod(
                user.getId(), period, startDate, today, pageable
        );

        // ID가 없으면 빈 결과 반환
        if (orderIdsPage.isEmpty())
            return Page.empty(pageable);

        // 두번째 쿼리: ID 목록으로 상세 데이터 조회
        List<OrderResponse> orderResponses = getOrderResponses(user, orderIdsPage.getContent());

        return new PageImpl<>(orderResponses, pageable, orderIdsPage.getTotalElements());
    }

    // 회원 주문 취소 상세 페이지
    @Override
    public OrderCancelResponse getCancelPage(User user, Long orderId) {
        Order order = orderRepository.findByIdWithDetailsAndProducts(user.getId(), orderId)
                .orElseThrow(() -> new DomainException(ErrorType.ORDER_NOT_FOUND));

        OrderDetailPageResponse pageResponse = createOrderDetailPageResponse(order, user);

        return new OrderCancelResponse(order.getUpdatedAt(), pageResponse);
    }

    //리뷰 작성 시 필요한 상품 정보 내려주기 API
    @Override
    public OrderProductResponse getItem(Long orderDetailId) {
        OrderDetail orderDetail = orderDetailRepository.findOrderDetailForReviewById(orderDetailId)
                .orElseThrow(() -> new DomainException(ErrorType.ORDER_DETAIL_NOT_FOUND));
        
        OrderProductResponse response = OrderProductResponse.from(orderDetail, true);

        return response;
    }

    //회원 주문서로 넘어가는 API
    @Override
    public CartResponse getCheckout(User user, List<OrderDetailRequest> orderDetailRequest) {
        List<Long> saleProductIds = orderDetailRequest.stream()
                .map(OrderDetailRequest::saleProductId)
                .toList();

        List<SaleProduct> saleProducts = saleProductRepository.findAllById(saleProductIds);

        List<CartDetailResponse> cartDetails = saleProducts.stream()
                .map(product -> {
                    var matchingRequest = orderDetailRequest.stream()
                            .filter(request -> request.saleProductId().equals(product.getId()))
                            .findFirst()
                            .orElseThrow(() -> new DomainException(ErrorType.PRODUCT_NOT_FOUND));

                    // 해당 상품의 수량을 찾음
                    int quantity = orderDetailRequest.stream()
                            .filter(request -> request.saleProductId().equals(product.getId()))
                            .findFirst()
                            .map(OrderDetailRequest::quantity)
                            .orElseThrow(() -> new DomainException(ErrorType.INVALID_QUANTITY)); // 기본값은 1로 설정

                    int paymentPrice = product.getProduct().getProductInfo().getDiscountPrice();
                    int orderPrice = product.getProduct().getProductInfo().getOriginPrice();
                    int discountPrice = orderPrice - paymentPrice;

                    return CartDetailResponse.from(
                            // 장바구니에서 넘어온 것이므로 cartId는 null
                            matchingRequest.cartId(),
                            product,
                            quantity,
                            paymentPrice,
                            orderPrice,
                            discountPrice,
                            false
                    );
                })
                .toList();

        // CartResponse 생성 및 반환
        return CartResponse.of(cartDetails);
    }

    private OrderDetailPageResponse createOrderDetailPageResponse(Order order, User user) {
        boolean isPurchasedConfirmed = order.getStatus() == OrderStatus.PURCHASED_CONFIRMED;

        List<OrderProductResponse> products = order.getOrderDetails().stream()
                .map(detail -> {
                    boolean canReview = isPurchasedConfirmed && !reviewRepository.existsByOrderDetailId(detail.getId());

                    return OrderProductResponse.from(detail, canReview);
                })
                .toList();

        return OrderDetailPageResponse.from(order, user, products);
    }

    private List<OrderResponse> getOrderResponses(User user, List<Long> orderIds) {
        List<Order> orders = orderRepository.findOrdersWithDetailsByIds(orderIds);

        // ID 순서에 맞게 정렬(두 번째 쿼리 결과의 순서를 첫 번째 쿼리와 일치시키기)
        Map<Long, Order> orderMap = orders.stream()
                .collect(Collectors.toMap(Order::getId, order -> order));

        return orderIds.stream()
                .map(orderMap::get)
                .filter(Objects::nonNull)
                .map(order -> createOrderResponse(order, user))
                .toList();
    }

    private OrderResponse createOrderResponse(Order order, User user) {
        List<OrderProductResponse> orderProductResponses = orderUtilService.convertToOrderDetailResponses(order.getOrderDetails(), user);

        return OrderResponse.fromOrder(order, orderProductResponses);
    }
}
