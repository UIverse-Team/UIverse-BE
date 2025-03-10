package com.jishop.order.dto;

import java.util.List;

public record OrderResponse(
        Long id,
        List<OrderDetailResponse> orderDetailResponseList
){}
