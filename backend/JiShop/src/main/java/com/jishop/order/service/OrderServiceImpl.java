package com.jishop.order.service;

import com.jishop.address.domain.Address;
import com.jishop.address.dto.AddressRequest;
import com.jishop.address.repository.AddressRepository;
import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.member.domain.User;
import com.jishop.order.domain.Order;
import com.jishop.order.domain.OrderDetail;
import com.jishop.order.domain.OrderStatus;
import com.jishop.order.dto.*;
import com.jishop.order.repository.OrderRepository;
import com.jishop.review.repository.ReviewRepository;
import com.jishop.saleproduct.domain.SaleProduct;
import com.jishop.saleproduct.repository.SaleProductRepository;
import com.jishop.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final SaleProductRepository saleProductRepository;
    private final StockService stockService;
    private final ReviewRepository reviewRepository;
    private final AddressRepository addressRepository;

    //주문 생성
    @Override
    @Transactional
    public OrderResponse createOrder(User user, OrderRequest orderRequest) {
        // 주소 저장 (회원인 경우만)
        if (user != null) {
            addressRepository.save(orderRequest.address().toEntity(user, false));
        }

        // 주문 번호 생성
        String orderNumberStr = generateOrderNumber();

        // 주문 객체 생성
        Order order;
        if (user != null) {
            order = orderRequest.toEntity(user).withOrderNumber(orderNumberStr);
        } else {
            order = Order.builder()
                    .userId(null)
                    .recipient(orderRequest.address().recipient())
                    .phone(orderRequest.address().phone())
                    .address(orderRequest.address().address())
                    .detailAddress(orderRequest.address().detailAddress())
                    .zonecode(orderRequest.address().zonecode())
                    .orderNumber(orderNumberStr)
                    .build();
        }

        // SaleProduct 일괄 조회 및 맵 생성
        List<Long> saleProductIds = orderRequest.orderDetailRequestList().stream()
                .map(OrderDetailRequest::saleProductId)
                .toList();

        List<SaleProduct> saleProducts = saleProductRepository.findAllByIdsForOrder(saleProductIds);
        Map<Long, SaleProduct> saleProductMap = saleProducts.stream()
                .collect(Collectors.toMap(SaleProduct::getId, sp -> sp));

        // 주문 상세 처리
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (OrderDetailRequest detailRequest : orderRequest.orderDetailRequestList()) {
            Long saleProductId = detailRequest.saleProductId();
            int quantity = detailRequest.quantity();

            SaleProduct saleProduct = saleProductMap.get(saleProductId);
            if (saleProduct == null) throw new DomainException(ErrorType.PRODUCT_NOT_FOUND);

            stockService.decreaseStock(saleProduct.getStock(), quantity);

            int paymentPrice = saleProduct.getProduct().getDiscountPrice();
            int orderPrice = saleProduct.getProduct().getOriginPrice();
            int discountPrice = orderPrice - paymentPrice;

            if(saleProduct.getOption() != null){
                paymentPrice += saleProduct.getOption().getOptionExtra();
                orderPrice += saleProduct.getOption().getOptionExtra();
            }

            OrderDetail orderDetail = OrderDetail.builder()
                    .order(order)
                    .orderNumber(orderNumberStr)
                    .saleProduct(saleProduct)
                    .quantity(quantity)
                    .paymentPrice(paymentPrice)
                    .orderPrice(orderPrice)
                    .discountPrice(discountPrice)
                    .build();

            orderDetails.add(orderDetail);
        }

        // 주문 정보 업데이트
        int totalPrice = orderDetails.stream()
                .mapToInt(detail -> detail.getPaymentPrice() * detail.getQuantity())
                .sum();

        order.updateOrderInfo(totalPrice, orderDetails, orderNumberStr);
        orderRepository.save(order);

        // 응답 생성
        List<OrderDetailResponse> orderDetailResponseList = convertToOrderDetailResponses(order.getOrderDetails());
        return OrderResponse.fromOrder(order, orderDetailResponseList);
    }


    //주문 상세 페이지
    @Override
    @Transactional(readOnly = true)
    public List<OrderDetailResponse> getOrder(User user, Long orderId) {
        Order order = orderRepository.findByIdWithDetailsAndProducts(user.getId(), orderId)
                .orElseThrow(() -> new DomainException(ErrorType.ORDER_NOT_FOUND));

        return convertToOrderDetailResponses(order.getOrderDetails());
    }

    //주문 전체 조회 페이징 처리
    @Override
    public Page<OrderResponse> getPaginatedOrders(User user, String period, int page, int size) {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime startDate = today;
        switch (period){
            case "1month":
                startDate = today.minusMonths(1);
                break;
            case "6months":
                startDate = today.minusMonths(6);
                break;
            case "all":
            default:
                // LocalDateTime.MIN 대신 충분히 과거의 날짜를 사용
                startDate = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
                break;
        }

        //첫번째 쿼리: 페이징 된 ID 목록 가져오기
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Long> orderIdsPage = orderRepository.findOrderIdsByPeriod(
                user.getId(), period, startDate, today, pageable
        );

        //ID가 없으면 빈 결과 반환
        if(orderIdsPage.isEmpty())
            return Page.empty(pageable);

        //두번째 쿼리: ID 목록으로 상세 데이터 조회
        List<Long> orderIds = orderIdsPage.getContent();
        List<Order> orders = orderRepository.findOrdersWithDetailsByIds(orderIds);

        // ID 순서에 맞게 정렬(두 번째 쿼리 결과의 순서를 첫 번째 쿼리와 일치시키기)
        Map<Long, Order> orderMap = orders.stream()
                .collect(Collectors.toMap(Order::getId, order -> order));

        List<OrderResponse> orderResponses = orderIds.stream()
                .map(orderMap::get)
                .filter(Objects::nonNull)
                .map(order -> {
                    List<OrderDetailResponse> orderDetailResponses = convertToOrderDetailResponses(order.getOrderDetails());
                    return OrderResponse.fromOrder(order, orderDetailResponses);
                })
                .toList();
        return new PageImpl<>(orderResponses, pageable, orderIdsPage.getTotalElements());
    }

    //회원 주문 취소
    @Override
    @Transactional
    public void cancelOrder(User user, Long orderId) {
        Order order = orderRepository.findByIdWithDetails(user.getId(), orderId)
                .orElseThrow(() -> new DomainException(ErrorType.ORDER_NOT_FOUND));

        //주문 취소가 가능한 지
        validateOrderCancellation(order);
        //재고 증가 및 주문 상태 변경
        processOrderCancellation(order);
    }

    //주문 취소가 가능한 지
    private void validateOrderCancellation(Order order) {
        if (order.getStatus() == OrderStatus.ORDER_CANCELED)
            throw new DomainException(ErrorType.ORDER_ALREADY_CANCELED);

        if (order.getStatus() == OrderStatus.PURCHASED_CONFIRMED)
            throw new DomainException(ErrorType.ORDER_ALREADY_CONFIRMED);
    }

    //재고 증가 및 주문 상태 변경
    private void processOrderCancellation(Order order) {
        // 재고 복구
        for (OrderDetail orderDetail : order.getOrderDetails()) {
            SaleProduct saleProduct = orderDetail.getSaleProduct();
            int quantity = orderDetail.getQuantity();
            stockService.increaseStock(saleProduct.getStock(), quantity);
        }

        // 주문 상태 변경
        order.updateStatus(OrderStatus.ORDER_CANCELED);
        order.delete();
    }

    //회원 바로 주문하기
    @Override
    public OrderResponse createInstantOrder(User user, InstantOrderRequest instantOrderRequest) {
        // 상품 정보 조회
        SaleProduct saleProduct = saleProductRepository.findById(instantOrderRequest.saleProductId())
                .orElseThrow(() -> new DomainException(ErrorType.PRODUCT_NOT_FOUND));

        // 주문 상세 요청 생성
        OrderDetailRequest orderDetailRequest = new OrderDetailRequest(
                saleProduct.getId(), saleProduct.getName(), instantOrderRequest.quantity()
        );

        // 주소 저장 및 요청 생성
        Address shippingAddress = instantOrderRequest.address().toEntity(user, false);
        addressRepository.save(shippingAddress);

        AddressRequest addressRequest = instantOrderRequest.address();

        // 주문 요청 생성 및 주문 처리
        OrderRequest orderRequest = new OrderRequest(
                addressRequest,
                List.of(orderDetailRequest)
        );

        return createOrder(user, orderRequest);
    }

    //비회원 주문 생성
    @Override
    public OrderResponse createGuestOrder(OrderRequest orderRequest) {
        // 회원 주문 생성 메서드 재사용 (user = null)
        OrderResponse response = createOrder(null, orderRequest);

        return response;
    }

    //회원 주문 조회
    @Override
    @Transactional
    public List<OrderDetailResponse> getGuestOrder(String orderNumber, String phone) {
        Order order = orderRepository.findByOrderNumberAndPhone(orderNumber, phone)
                .orElseThrow(() -> new DomainException(ErrorType.ORDER_NOT_FOUND));

        return convertToOrderDetailResponses(order.getOrderDetails());
    }

    // 비회원 바로 주문
    @Override
    @Transactional
    public OrderResponse createGuestInstantOrder(InstantOrderRequest orderRequest) {
        // 상품 정보 조회
        SaleProduct saleProduct = saleProductRepository.findById(orderRequest.saleProductId())
                .orElseThrow(() -> new DomainException(ErrorType.PRODUCT_NOT_FOUND));

        // 주문 상세 요청 생성
        OrderDetailRequest orderDetailRequest = new OrderDetailRequest(
                saleProduct.getId(), saleProduct.getName(), orderRequest.quantity()
        );

        // 주소 요청 생성
        AddressRequest addressRequest = new AddressRequest(
                orderRequest.address().recipient(),
                orderRequest.address().phone(),
                orderRequest.address().zonecode(),
                orderRequest.address().address(),
                orderRequest.address().detailAddress(),
                false
        );

        // 주문 요청 생성 및 주문 처리
        OrderRequest request = new OrderRequest(
                addressRequest,
                List.of(orderDetailRequest)
        );

        // 회원 주문 생성 메서드 재사용 (user = null)
        OrderResponse response = createOrder(null, request);
        return response;
    }

    //비회원 주문 취소
    @Override
    @Transactional
    public void cancelGuestOrder(String orderNumber, String phone) {
        Order order = orderRepository.findByOrderNumberAndPhone(orderNumber, phone)
                .orElseThrow(() -> new DomainException(ErrorType.ORDER_NOT_FOUND));

        validateOrderCancellation(order);
        processOrderCancellation(order);
    }

    private List<OrderDetailResponse> convertToOrderDetailResponses(List<OrderDetail> details) {
        // 주문 상태가 구매확정인 주문만 리뷰 작성 가능
        boolean isPurchaseConfirmed = !details.isEmpty() &&
                details.get(0).getOrder().getStatus() == OrderStatus.PURCHASED_CONFIRMED;

        if (!isPurchaseConfirmed) {
            return details.stream()
                    .map(detail -> new OrderDetailResponse(
                            detail.getId(),
                            detail.getOrderNumber(),
                            detail.getSaleProduct().getId(),
                            detail.getSaleProduct().getName(),
                            detail.getSaleProduct().getOption() != null ? detail.getSaleProduct().getOption().getOptionValue() : null,
                            detail.getPaymentPrice(),
                            detail.getOrderPrice(),
                            detail.getDiscountPrice(),
                            detail.getQuantity(),
                            detail.getPaymentPrice() * detail.getQuantity(),
                            false, // 구매확정 상태가 아니면 리뷰 작성 불가
                            detail.getSaleProduct().getProduct().getBrand(),
                            detail.getOrder().getCreatedAt()
                    ))
                    .toList();
        }

        // 모든 주문 상세 ID 목록 => 한번의 쿼리로 리뷰가 작성된 거 까지 가져오고, 리뷰 작성 여부는 여기서 확인(N+1 문제 해결)
        List<Long> orderDetailIds = details.stream().map(OrderDetail::getId).toList();

        // 리뷰가 있는 주문 상세 ID 목록
        List<Long> reviewedOrderDetailIds = reviewRepository.findOrderDetailIdsWithReviews(orderDetailIds);

        return details.stream()
                .map(detail -> new OrderDetailResponse(
                        detail.getId(),
                        detail.getOrderNumber(),
                        detail.getSaleProduct().getId(),
                        detail.getSaleProduct().getName(),
                        detail.getSaleProduct().getOption() != null ? detail.getSaleProduct().getOption().getOptionValue() : null,
                        detail.getPaymentPrice(),
                        detail.getOrderPrice(),
                        detail.getDiscountPrice(),
                        detail.getQuantity(),
                        detail.getPaymentPrice() * detail.getQuantity(),
                        !reviewedOrderDetailIds.contains(detail.getId()), // 리뷰가 없는 경우만 true
                        detail.getSaleProduct().getProduct().getBrand(),
                        detail.getOrder().getCreatedAt()
                ))
                .toList();
    }

    //
    private int calculatePrice(SaleProduct saleProduct) {
        int price = saleProduct.getProduct().getDiscountPrice();

        if(saleProduct.getOption() != null) {
            price += saleProduct.getOption().getOptionExtra();
        }

        return price;
    }

    private static final int LENGTH = 5;
    private static final String CHARACTERS = "01346789ABCDFGHJKMNPQRSTUVWXYZ";

    //주문 번호 생성
    private String generateOrderNumber() {
        String orderTypeCode = "O"; //Order
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        String formattedDate = currentDate.format(formatter);

        String randomStr = "";
        Random random = new Random();

        do{
            StringBuilder sb = new StringBuilder(LENGTH);
            for(int i = 0; i < LENGTH; i++){
                int randomIndex = random.nextInt(CHARACTERS.length());
                char randomChar = CHARACTERS.charAt(randomIndex);
                sb.append(randomChar);
            }
            randomStr = sb.toString();
        } while(orderRepository.existsByOrderNumber(orderTypeCode + formattedDate + randomStr));

        return orderTypeCode + formattedDate + randomStr;
    }
}