package com.jishop.order.service;

import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.member.domain.User;
import com.jishop.member.repository.UserRepository;
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
import org.apache.velocity.runtime.directive.Foreach;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final SaleProductRepository saleProductRepository;
    private final StockService stockService;
    private final ReviewRepository reviewRepository;

    //주문 생성
    @Override
    @Transactional
    public OrderResponse createOrder(User user, OrderRequest orderRequest) {
        // 주문 번호 생성 (저장하지 않고 문자열만 받음)
        String orderNumberStr = generateOrderNumber();

        // 주문 객체 생성
        Order order = orderRequest.toEntity(user);

        // SaleProduct ID 리스트 추출
        List<Long> saleProductIds = orderRequest.orderDetailRequestList().stream()
                .map(OrderDetailRequest::saleProductId)
                .toList();

        // SaleProduct 일괄 조회 (N+1 문제 방지)
        List<SaleProduct> saleProducts = saleProductRepository.findAllByIdsForOrder(saleProductIds);

        // ID로 SaleProduct 조회를 위한 Map 생성
        Map<Long, SaleProduct> saleProductMap = saleProducts.stream()
                .collect(Collectors.toMap(SaleProduct::getId, sp -> sp));

        List<OrderDetail> orderDetails = new ArrayList<>();

        int totalPrice = 0;
        String mainProductName = "";

        List<OrderDetailRequest> orderDetailRequests = orderRequest.orderDetailRequestList();

        for(OrderDetailRequest detailRequest : orderDetailRequests){
            Long saleProductId = detailRequest.saleProductId();
            int quantity = detailRequest.quantity();

            //상품 조회 (맵에서 조회)
            SaleProduct saleProduct = saleProductMap.get(saleProductId);
            if(saleProduct == null) {
                throw new DomainException(ErrorType.PRODUCT_NOT_FOUND);
            }

            // 재고 감소 처리 (재고 부족 시 예외 터짐)
            stockService.decreaseStock(saleProduct.getStock(), quantity);

            // 옵션 추가 금액 계산
            int price = saleProduct.getProduct().getDiscountPrice();
            if(saleProduct.getOption() != null){
                price += saleProduct.getOption().getOptionExtra();
            }

            // 주문 상세 생성
            OrderDetail orderDetail = OrderDetail.builder()
                    .order(order)
                    .saleProduct(saleProduct)
                    .quantity(quantity)
                    .price(price)
                    .build();

            orderDetails.add(orderDetail);
            totalPrice += price * quantity;
        }

        // 주문 상세가 1건일 경우
        mainProductName = orderDetails.get(0).getSaleProduct().getName();

        // 주문 상세가 2건 이상일 경우
        if(orderDetailRequests.size() != 1)
            mainProductName = mainProductName + " 외 " + (orderDetailRequests.size()-1) + "건";

        // 주문 정보 업데이트
        order.updateOrderInfo(mainProductName, totalPrice, orderDetails, orderNumberStr);

        // 주문 저장
        orderRepository.save(order);

        List<OrderDetailResponse> orderDetailResponseList = convertToOrderDetailResponses(order.getOrderDetails());

        return OrderResponse.fromOrder(order, orderDetailResponseList);
    }

    //주문 단건 조회
    @Override
    @Transactional(readOnly = true)
    public List<OrderDetailResponse> getOrder(User user, Long orderId){
        Order order = orderRepository.findByIdWithDetailsAndProducts(user.getId(), orderId)
                .orElseThrow(() -> new DomainException(ErrorType.ORDER_NOT_FOUND));

        return convertToOrderDetailResponses(order.getOrderDetails());
    }

    // 주문 내역 전체 조회
    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders(User user, String period) {
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

        List<Order> orders = orderRepository.findAllWithDetailsByPeriod(user.getId(), period, startDate, today);

        return orders.stream()
                .map(order -> {
                    List<OrderDetailResponse> orderDetailResponseList = convertToOrderDetailResponses(order.getOrderDetails());
                    return OrderResponse.fromOrder(order, orderDetailResponseList);
                })
                .toList();
    }

    //주문취소
    @Override
    @Transactional
    public void cancelOrder(User user, Long orderId){
        Order order = orderRepository.findByIdWithDetails(user.getId(), orderId)
                .orElseThrow(() -> new DomainException(ErrorType.ORDER_NOT_FOUND));

        if(order.getStatus() == OrderStatus.ORDER_CANCELED)
            throw new DomainException(ErrorType.ORDER_ALREADY_CANCELED);

        if(order.getStatus() == OrderStatus.PURCHASED_CONFIRMED)
            throw new DomainException(ErrorType.ORDER_ALREADY_CONFIRMED);

        List<OrderDetail> orderDetails = order.getOrderDetails();
        for(OrderDetail orderDetail : orderDetails){
            // OrderDetail의 SaleProduct에서 Stock을 직접 사용
            SaleProduct saleProduct = orderDetail.getSaleProduct();
            int quantity = orderDetail.getQuantity();

            // ID 대신 Stock 객체를 직접 전달
            stockService.increaseStock(saleProduct.getStock(), quantity);
        }
        //주문 상태 변경
        order.updateStatus(OrderStatus.ORDER_CANCELED);
        order.delete();
    }

    // 바로 구매하기
    @Override
    public OrderResponse createInstantOrder(User user, InstantOrderRequest instantOrderRequest) {
        //상품 정보 조회
        SaleProduct saleProduct = saleProductRepository.findById(instantOrderRequest.saleProductId())
                .orElseThrow(() -> new DomainException(ErrorType.PRODUCT_NOT_FOUND));

        OrderDetailRequest orderDetailRequest = new OrderDetailRequest(
                saleProduct.getId(), saleProduct.getName(), instantOrderRequest.quantity()
        );

        //todo: 사용자의 주소지 끌고와서 넣기
        OrderRequest orderRequest = new OrderRequest(
                "receiver" + user.getName(),
                "phone" + user.getPhone(),
                "baseAddress",
                "detailAddress",
                "zipCode",
                List.of(orderDetailRequest)
        );

        return createOrder(user, orderRequest);
    }

    private List<OrderDetailResponse> convertToOrderDetailResponses(List<OrderDetail> details) {
        // 주문 상태가 구매확정인 주문만 리뷰 작성 가능
        boolean isPurchaseConfirmed = !details.isEmpty() &&
                details.get(0).getOrder().getStatus() == OrderStatus.PURCHASED_CONFIRMED;

        if (!isPurchaseConfirmed) {
            return details.stream()
                    .map(detail -> new OrderDetailResponse(
                            detail.getId(),
                            detail.getSaleProduct().getId(),
                            detail.getSaleProduct().getName(),
                            detail.getSaleProduct().getOption() != null ? detail.getSaleProduct().getOption().getOptionValue() : null,
                            detail.getPrice(),
                            detail.getQuantity(),
                            detail.getPrice() * detail.getQuantity(),
                            false // 구매확정 상태가 아니면 리뷰 작성 불가
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
                        detail.getSaleProduct().getId(),
                        detail.getSaleProduct().getName(),
                        detail.getSaleProduct().getOption() != null ? detail.getSaleProduct().getOption().getOptionValue() : null,
                        detail.getPrice(),
                        detail.getQuantity(),
                        detail.getPrice() * detail.getQuantity(),
                        !reviewedOrderDetailIds.contains(detail.getId()) // 리뷰가 없는 경우만 true
                ))
                .toList();
    }


    private static final int LENGTH = 5;
    private static final String CHARACTERS = "01346789ABCDFGHJKMNPQRSTUVWXYZ";

    private String generateOrderNumber() {
        /*
         * 주문 번호 생성 로직
         * 포맷은 TYYMMDDXXXXXX 이다.
         * T : Type (O : Order)
         * YY : 년도
         * MM : 월
         * DD : 일
         * XXXXX : 5자리 영문자 + 숫자
         */

        String orderTypeCode = "O"; //Order
        //년도와 날짜 정보를 담는다: yymmdd 형식
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        String formattedDate = currentDate.format(formatter);

        //5자리 정수를 만든 후, 해당 정수가 존재하는지 확인을 반복한다
        String randomStr = "";
        Random random = new Random();

        do{
            //숫자, 알파벳 대소문자로 이루어진 5자리 랜덤 문자열 생성
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