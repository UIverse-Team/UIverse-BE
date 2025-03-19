package com.jishop.order.dto;

import com.jishop.order.domain.Order;
import com.jishop.order.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        String orderNumber,
        List<OrderDetailResponse> orderDetailResponseList,
        OrderStatus orderStatus,
        int totalPrice,
        LocalDateTime createdAt,
        int totalQuantity
){
    public static OrderResponse fromOrder(Order order, List<OrderDetailResponse> orderDetailResponseList) {
        int totalQuantity = orderDetailResponseList.stream()
                .mapToInt(OrderDetailResponse::quantity)
                .sum();

        return new OrderResponse(
                order.getId(),
                order.getOrderNumber(),
                orderDetailResponseList,
                order.getStatus(),
                order.getTotalPrice(),
                order.getCreatedAt(),
                totalQuantity
        );
    }
}