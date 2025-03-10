package com.jishop.order.dto;

import com.jishop.order.domain.OrderStatus;

import java.util.List;

public record OrderResponse(
        Long id,
        List<OrderDetailResponse> orderDetailResponseList,
        OrderStatus orderStatus,
        String mainProductName,
        int totalPrice
){}
