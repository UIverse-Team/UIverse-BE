package com.jishop.order.service;

import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.order.domain.Order;
import com.jishop.order.domain.OrderDetail;
import com.jishop.order.domain.OrderStatus;
import com.jishop.domain.Product;
import com.jishop.order.dto.OrderDetailRequest;
import com.jishop.order.dto.OrderDetailResponse;
import com.jishop.order.dto.OrderRequest;
import com.jishop.order.dto.OrderResponse;
import com.jishop.order.repository.OrderRepository;
import com.jishop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    //주문 생성
    @Override
    public void createOrder(OrderRequest orderRequest) {
        Order order = orderRequest.toEntity();

        List<OrderDetail> orderDetails = new ArrayList<>();
        int totalPrice = 0;
        String mainProductName = "";
        boolean isFirstProduct = true;
        List<OrderDetailRequest> orderDetailRequests = orderRequest.orderDetailRequestList();

        for(OrderDetailRequest detailRequest : orderDetailRequests){
            Long productId = detailRequest.productId();
            int quantity = detailRequest.quantity();

            //상품 조회
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new DomainException(ErrorType.PRODUCT_NOT_FOUND));

            // 주문 상세 생성
            OrderDetail orderDetail = OrderDetail.builder()
                    .order(order)
                    .product(product)
                    .quantity(quantity)
                    .price(product.getPrice())
                    .build();

            orderDetails.add(orderDetail);
            totalPrice += product.getPrice();

            if(isFirstProduct){
                mainProductName = product.getName() + " 외 " + (orderDetailRequests.size()-1) + "건";
                isFirstProduct = false;
            }
        }
        //주문 정보 업데이트
        order.updateOrderInfo(mainProductName, totalPrice, orderDetails);

        //주문 저장
        orderRepository.save(order);

        //응답 생성 이 메서드에서 사용할 일이 없는 듯?
//        List<OrderDetailResponse> orderDetailResponses = savedOrder.getOrderDetails().stream()
//                .map(detail -> new OrderDetailResponse(detail.getId(), detail.getProduct().getId()))
//                .collect(Collectors.toList());
    }

    //주문 단건 조회
    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new DomainException(ErrorType.ORDER_NOT_FOUND));

        List<OrderDetailResponse> orderDetailResponseList = order.getOrderDetails().stream()
                .map(detail -> new OrderDetailResponse(detail.getId(), detail.getProduct().getId()))
                .toList();

        return new OrderResponse(order.getId(), orderDetailResponseList);
    }

    // 주문 내역 전체 조회
    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(order -> {
                    List<OrderDetailResponse> orderDetailResponseList = order.getOrderDetails().stream()
                            .map(detail -> new OrderDetailResponse(detail.getId(), detail.getProduct().getId()))
                            .toList();
                    return new OrderResponse(order.getId(), orderDetailResponseList);
                })
                .toList();

    }

   //주문취소
    @Override
    public void cancelOrder(Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new DomainException(ErrorType.ORDER_NOT_FOUND));

        if(order.getStatus() == OrderStatus.ORDER_CANCELED)
            throw new IllegalStateException("이미 취소된 주문입니다");

        if(order.getStatus() == OrderStatus.PURCHASED_CONFIRMED)
            throw new IllegalStateException("이미 구매 확정된 주문은 취소할 수 없습니다");

        //주문 상태 변경
        order.updateStatus(OrderStatus.ORDER_CANCELED);
    }

}
