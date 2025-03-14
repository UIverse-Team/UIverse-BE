package com.jishop.order.dto;

public record OrderDetailResponse(
    Long id,
    Long saleProductId,
    String productName,
    String optionValue,
    int price,
    int quantity,
    int totalPrice
) {
}
