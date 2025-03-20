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
        int totalQuantity = orderProducts.stream()
                .mapToInt(OrderProductResponse::quantity)
                .sum();

        return new OrderResponse(
                order.getId(),
                order.getOrderNumber(),
                orderProducts,
                order.getStatus(),
                order.getTotalPrice(),
                order.getCreatedAt(),
                totalQuantity
        );
    }
}