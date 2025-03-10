package com.jishop.order.dto;

public record OrderDetailRequest(
    Long productId,
    int quantity
) {
}
