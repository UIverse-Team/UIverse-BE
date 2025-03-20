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

        //주문 entity 생성
        Order order = createOrderEntity(user, orderRequest);

        //orderDetail 생성, 수량 업데이트, 금액 계산
        List<OrderDetail> orderDetails = processOrderDetails(order, orderRequest.orderDetailRequestList());

        //주문(Order)에 detail, 총 금액 업데이트
        int totalPrice = calculateTotalPrice(orderDetails);
        order.updateOrderInfo(totalPrice, orderDetails, order.getOrderNumber());
        orderRepository.save(order);

        //Response 반환
        List<OrderProductResponse> orderProductResponses = convertToOrderDetailResponses(order.getOrderDetails(), user);

        return OrderResponse.fromOrder(order, orderProductResponses);
    }

    //회원 바로 주문하기
    @Override
    public OrderResponse createInstantOrder(User user, InstantOrderRequest instantOrderRequest) {
        //InstantOrderRequest => OrderRequest
        OrderRequest orderRequest = convertInstantToOrderRequest(instantOrderRequest);

        return createOrder(user, orderRequest);
    }

    //비회원 주문 생성
    @Override
    public OrderResponse createGuestOrder(OrderRequest orderRequest) {
        // 회원 주문 생성 메서드 재사용 (user = null)
        return createOrder(null, orderRequest);
    }

    // 비회원 바로 주문
    @Override
    @Transactional
    public OrderResponse createGuestInstantOrder(InstantOrderRequest orderRequest) {
        //InstantOrderRequest => OrderRequest
        OrderRequest guestInstantOrder = convertInstantToOrderRequest(orderRequest);

        return createOrder(null, guestInstantOrder);
    }

    //주문 상세 페이지
    @Override
    @Transactional(readOnly = true)
    public OrderDetailPageResponse getOrder(User user, Long orderId) {
        Order order = orderRepository.findByIdWithDetailsAndProducts(user.getId(), orderId)
                .orElseThrow(() -> new DomainException(ErrorType.ORDER_NOT_FOUND));

        return createOrderDetailPageResponse(order, user);
    }

    //회원 주문 조회
    @Override
    @Transactional
    public OrderDetailPageResponse getGuestOrder(String orderNumber, String phone) {
        Order order = orderRepository.findByOrderNumberAndPhone(orderNumber, phone)
                .orElseThrow(() -> new DomainException(ErrorType.ORDER_NOT_FOUND));

        return createOrderDetailPageResponse(order, null);
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
                    List<OrderProductResponse> orderDetailResponses = convertToOrderDetailResponses(order.getOrderDetails(), user);
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

        processCancellation(order);
    }

    //비회원 주문 취소
    @Override
    @Transactional
    public void cancelGuestOrder(String orderNumber, String phone) {
        Order order = orderRepository.findByOrderNumberAndPhone(orderNumber, phone)
                .orElseThrow(() -> new DomainException(ErrorType.ORDER_NOT_FOUND));

        processCancellation(order);
    }

    private Order createOrderEntity(User user, OrderRequest orderRequest) {
        String orderNumber = generateOrderNumber();

        Order order;
        if(user != null){
            order = orderRequest.toEntity(user).withOrderNumber(orderNumber);
        } else {
            order = Order.builder()
                    .userId(null)
                    .recipient(orderRequest.address().recipient())
                    .phone(orderRequest.address().phone())
                    .address(orderRequest.address().address())
                    .detailAddress(orderRequest.address().detailAddress())
                    .zonecode(orderRequest.address().zonecode())
                    .orderNumber(orderNumber)
                    .build();
        }

        return order;
    }

    private List<OrderDetail> processOrderDetails(Order order, List<OrderDetailRequest> orderDetailRequestList) {
        List<Long> saleProductIds = orderDetailRequestList.stream()
                .map(OrderDetailRequest::saleProductId)
                .toList();

        List<SaleProduct> saleProducts = saleProductRepository.findAllByIdsForOrder(saleProductIds);
        Map<Long, SaleProduct> saleProductMap = saleProducts.stream()
                .collect(Collectors.toMap(SaleProduct::getId, sp -> sp));

        List<OrderDetail> orderDetails = new ArrayList<>();

        for(OrderDetailRequest orderDetailRequest : orderDetailRequestList) {
            SaleProduct saleProduct = saleProductMap.get(orderDetailRequest.saleProductId());
            if(saleProduct == null) {
                throw new DomainException(ErrorType.PRODUCT_NOT_FOUND);
            }

            //수량 줄이기
            stockService.decreaseStock(saleProduct.getStock(), orderDetailRequest.quantity());

            // 가격 계산
            int paymentPrice = saleProduct.getProduct().getDiscountPrice();
            int orderPrice = saleProduct.getProduct().getOriginPrice();
            int discountPrice = orderPrice - paymentPrice;

            if (saleProduct.getOption() != null) {
                int optionExtra = saleProduct.getOption().getOptionExtra();
                paymentPrice += optionExtra;
                orderPrice += optionExtra;
            }

            // Create order detail
            OrderDetail orderDetail = OrderDetail.builder()
                    .order(order)
                    .orderNumber(order.getOrderNumber())
                    .saleProduct(saleProduct)
                    .quantity(orderDetailRequest.quantity())
                    .paymentPrice(paymentPrice)
                    .orderPrice(orderPrice)
                    .discountPrice(discountPrice)
                    .build();

            orderDetails.add(orderDetail);
        }

        return orderDetails;
    }


    private int calculateTotalPrice(List<OrderDetail> orderDetails) {
        return orderDetails.stream()
                .mapToInt(detail -> detail.getPaymentPrice() * detail.getQuantity())
                .sum();
    }

    private OrderRequest convertInstantToOrderRequest(InstantOrderRequest instantOrderRequest) {
        SaleProduct saleProduct = saleProductRepository.findById(instantOrderRequest.saleProductId())
                .orElseThrow(() -> new DomainException(ErrorType.PRODUCT_NOT_FOUND));

        OrderDetailRequest detailRequest = new OrderDetailRequest(
            saleProduct.getId(), instantOrderRequest.quantity()
        );

        return new OrderRequest(
                instantOrderRequest.address(),
                List.of(detailRequest)
        );
    }

    private OrderDetailPageResponse createOrderDetailPageResponse(Order order, User user) {
        List<OrderProductResponse> products = order.getOrderDetails().stream()
                .map(detail -> {
                    boolean isPurchasedConfirmed = order.getStatus() == OrderStatus.PURCHASED_CONFIRMED;
                    boolean canReview = false;

                    if(isPurchasedConfirmed) {
                        canReview = !reviewRepository.existsByOrderDetailId(detail.getId());
                    }

                    return new OrderProductResponse(
                            detail.getId(),
                            detail.getSaleProduct().getId(),
                            detail.getSaleProduct().getName(),
                            detail.getSaleProduct().getOption() != null ? detail.getSaleProduct().getOption().getOptionValue() : null,
                            detail.getPaymentPrice(),
                            detail.getOrderPrice(),
                            detail.getDiscountPrice(),
                            detail.getQuantity(),
                            detail.getPaymentPrice() * detail.getQuantity(),
                            canReview,
                            detail.getSaleProduct().getProduct().getBrand()
                    );
                })
                .toList();

        return new OrderDetailPageResponse(
                order.getOrderNumber(),
                order.getStatus(),
                order.getTotalPrice(),
                order.getCreatedAt(),
                order.getRecipient(),
                order.getPhone(),
                order.getZonecode(),
                order.getAddress(),
                order.getDetailAddress(),
                user != null ? user.getLoginId() : null,
                products
        );
    }

    private void processCancellation(Order order) {
        validateOrderCancellation(order);

        // Restore stock
        for (OrderDetail orderDetail : order.getOrderDetails()) {
            SaleProduct saleProduct = orderDetail.getSaleProduct();
            int quantity = orderDetail.getQuantity();
            stockService.increaseStock(saleProduct.getStock(), quantity);
        }

        // Update order status
        order.updateStatus(OrderStatus.ORDER_CANCELED);
        order.delete();
    }

    private void validateOrderCancellation(Order order) {
        if (order.getStatus() == OrderStatus.ORDER_CANCELED) {
            throw new DomainException(ErrorType.ORDER_ALREADY_CANCELED);
        }

        if (order.getStatus() == OrderStatus.PURCHASED_CONFIRMED) {
            throw new DomainException(ErrorType.ORDER_ALREADY_CONFIRMED);
        }
    }

    private List<OrderProductResponse> convertToOrderDetailResponses(List<OrderDetail> details, User user) {
        // 주문 상태가 구매확정인 주문만 리뷰 작성 가능
        boolean isPurchaseConfirmed = !details.isEmpty() &&
                details.get(0).getOrder().getStatus() == OrderStatus.PURCHASED_CONFIRMED;

        List<Long> orderDetailIds = details.stream().map(OrderDetail::getId).toList();
        List<Long> reviewedOrderDetailIds = isPurchaseConfirmed ?
                reviewRepository.findOrderDetailIdsWithReviews(orderDetailIds) : Collections.emptyList();

        return details.stream()
                .map(detail -> new OrderProductResponse(
                        detail.getId(),
                        detail.getSaleProduct().getId(),
                        detail.getSaleProduct().getName(),
                        detail.getSaleProduct().getOption() != null ? detail.getSaleProduct().getOption().getOptionValue() : null,
                        detail.getPaymentPrice(),
                        detail.getOrderPrice(),
                        detail.getDiscountPrice(),
                        detail.getQuantity(),
                        detail.getPaymentPrice() * detail.getQuantity(),
                        isPurchaseConfirmed && !reviewedOrderDetailIds.contains(detail.getId()),
                        detail.getSaleProduct().getProduct().getBrand()
                ))
                .toList();
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