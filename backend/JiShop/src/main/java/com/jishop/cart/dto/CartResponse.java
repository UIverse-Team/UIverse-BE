package com.jishop.cart.dto;

import java.util.List;

public record CartResponse(
        List<CartDetailResponse> cartDetailResponseList,
        int totalItems,
        int totalOrderPrice,
        int totalDiscountPrice,
        int totalPaymentPrice
) {
    public static CartResponse of(List<CartDetailResponse> cartItems){

        int totalOrderPrice = cartItems.stream()
                .mapToInt(item -> item.quantity() * item.orderPrice())
                .sum();

        int totalDiscountPrice = cartItems.stream()
                .mapToInt(item -> item.quantity() * item.discountPrice())
                .sum();

        int totalPaymentPrice = cartItems.stream()
                .mapToInt(item -> item.quantity() * item.paymentPrice())
                .sum();

        return new CartResponse(cartItems, cartItems.size(), totalOrderPrice, totalDiscountPrice, totalPaymentPrice);
    }
}
