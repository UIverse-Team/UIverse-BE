package com.jishop.cart.dto;

public record CartDetailResponse(
        Long cartId,
        Long saleProductId,
        String productName,
        String optionName,
        int paymentPrice, //결제금액 (원래가격 - 할인가격)
        int orderPrice, // 주문 금액 (원래가격)
        int discountPrice, //할인 가격
        int quantity,
        int totalPrice, //paymentPrice * quantity
        String image,
        String brandName
) {
}
