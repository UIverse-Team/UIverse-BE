package com.jishop.order.dto;

public record OrderDetailRequest(
    Long productId,
    String productName,
    int quantity
) {
}
