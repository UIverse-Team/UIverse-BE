package com.jishop.cart.dto;

public record CartDetailResponse(
        Long cartId,
        Long saleProductId,
        String productName,
        String optionName,
        int quantity,
        int price,
        String image
) {
}
