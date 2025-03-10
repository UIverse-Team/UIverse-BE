package com.jishop.order.dto;

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
){}
