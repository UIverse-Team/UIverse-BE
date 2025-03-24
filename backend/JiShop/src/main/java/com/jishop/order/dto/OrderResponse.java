package com.jishop.order.dto;

import com.jishop.order.domain.Order;
import com.jishop.order.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        String orderNumber,
        List<OrderProductResponse> orderProducts,
        OrderStatus orderStatus,
        int totalPrice,
        LocalDateTime createdAt,
        int totalQuantity
){
    public static OrderResponse fromOrder(Order order, List<OrderProductResponse> orderProducts) {
        // 총 수량 계산
        int totalQuantity = orderProducts.stream()
                .mapToInt(OrderProductResponse::quantity)
                .sum();

        // 가격 계산
        int totalOrderPrice = orderProducts.stream()
                .mapToInt(item -> item.quantity() * item.orderPrice())
                .sum();

        int totalDiscountPrice = orderProducts.stream()
                .mapToInt(item -> item.quantity() * item.discountPrice())
                .sum();

        int totalPaymentPrice = orderProducts.stream()
                .mapToInt(item -> item.quantity() * item.paymentPrice())
                .sum();

        // 필요한 경우 Order 객체 업데이트
            order.updateOrderInfo(totalOrderPrice, totalDiscountPrice, totalPaymentPrice, null, order.getOrderNumber());

        return new OrderResponse(
                order.getId(),
                order.getOrderNumber(),
                orderProducts,
                order.getStatus(),
                totalPaymentPrice,
                order.getCreatedAt(),
                totalQuantity
        );
    }
}