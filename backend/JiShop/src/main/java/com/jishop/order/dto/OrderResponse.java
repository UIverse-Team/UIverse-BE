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
        String receiver,
        String receiverNumber,
        String zipCode,
        String baseAddress,
        String detailAddress,
        LocalDateTime createdAt
){
    public static OrderResponse fromOrder(Order order, List<OrderDetailResponse> orderDetailResponseList) {
        return new OrderResponse(
                order.getId(),
                order.getOrderNumber(),
                orderDetailResponseList,
                order.getStatus(),
                order.getTotalPrice(),
                order.getRecipient(),
                order.getPhone(),
                order.getZonecode(),
                order.getAddress(),
                order.getDetailAddress(),
                order.getCreatedAt()
        );
    }
}