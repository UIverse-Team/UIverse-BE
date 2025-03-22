package com.jishop.order.dto;

import com.jishop.order.domain.OrderStatus;

import java.time.LocalDateTime;

public record OrderCancelResponse(
        LocalDateTime cancelTime,
        OrderDetailPageResponse pageResponse
) {
}
