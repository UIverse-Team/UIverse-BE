package com.jishop.order.dto;

import com.jishop.order.domain.Order;
import com.jishop.order.domain.OrderNumber;
import com.jishop.order.domain.OrderStatus;

import java.util.List;

public record OrderResponse(

        Long id,
        OrderNumber orderNumber,
        List<OrderDetailResponse> orderDetailResponseList,
        OrderStatus orderStatus,
        String mainProductName,
        int totalPrice
){
    public static OrderResponse fromOrder(Order order, List<OrderDetailResponse> orderDetailResponseList) {
        return new OrderResponse(
                order.getId(),
                order.getOrderNumber(),
                orderDetailResponseList,
                order.getStatus(),
                order.getMainProductName(),
                order.getTotalPrice()
        );
    }
}
