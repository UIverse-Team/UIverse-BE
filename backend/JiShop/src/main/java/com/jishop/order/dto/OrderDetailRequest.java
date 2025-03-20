package com.jishop.order.dto;

public record OrderDetailRequest(
        Long saleProductId,
        int quantity
) {
}
