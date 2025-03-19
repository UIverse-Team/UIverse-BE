package com.jishop.cart.dto;

import java.util.List;

public record CartResponse(
        List<CartDetailResponse> cartDetailResponseList,
        int totalItems,
        int totalPrice
) {
    public static CartResponse of(List<CartDetailResponse> cartItems){
        int totalPrice = cartItems.stream()
                .mapToInt(item -> item.quantity() * item.price())
                .sum();

        return new CartResponse(cartItems, cartItems.size(), totalPrice);
    }
}
