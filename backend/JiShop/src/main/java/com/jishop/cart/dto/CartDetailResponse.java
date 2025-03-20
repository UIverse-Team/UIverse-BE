package com.jishop.cart.dto;

public record CartDetailResponse(
        String productName,
        String optionName,
        int quantity,
        int price,
        String image
) {
}
