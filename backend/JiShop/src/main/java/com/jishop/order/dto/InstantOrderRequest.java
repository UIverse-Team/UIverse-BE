package com.jishop.order.dto;

public record InstantOrderRequest(
        Long saleProductId,
        int quantity
) {
}
