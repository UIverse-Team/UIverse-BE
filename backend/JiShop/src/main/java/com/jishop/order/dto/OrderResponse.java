package com.jishop.order.dto;

import com.jishop.order.domain.Order;
import com.jishop.order.domain.OrderStatus;

import java.util.List;

public record OrderResponse(
        Long id,
        String orderNumber,
        List<OrderDetailResponse> orderDetailResponseList,
        OrderStatus orderStatus,
        String mainProductName,
        int totalPrice,
        String receiver,
        String receiverNumber,
        String zipCode,
        String baseAddress,
        String detailAddress
){
    public static OrderResponse fromOrder(Order order, List<OrderDetailResponse> orderDetailResponseList) {
        return new OrderResponse(
                order.getId(),
                order.getOrderNumber().getOrderNumber(),
                orderDetailResponseList,
                order.getStatus(),
                order.getMainProductName(),
                order.getTotalPrice(),
                order.getReceiver(),
                order.getReceiverNumber(),
                order.getZipCode(),
                order.getBaseAddress(),
                order.getDetailAddress()
        );
    }
}