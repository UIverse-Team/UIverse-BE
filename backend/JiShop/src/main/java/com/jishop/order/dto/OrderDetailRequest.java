package com.jishop.order.dto;

public record OrderDetailRequest(
    Long saleProductId,
    String productName,
    int quantity
) {
}
