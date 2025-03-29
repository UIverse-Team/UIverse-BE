package com.jishop.cart.dto;

import com.jishop.saleproduct.domain.SaleProduct;

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
    public static CartDetailResponse from(
            Long cartId,
            SaleProduct product,
            int quantity,
            int paymentPrice,
            int orderPrice,
            int discountPrice
    ) {
        return new CartDetailResponse(
                cartId,
                product.getId(),
                product.getProduct().getName(),
                product.getOption() != null ? product.getOption().getOptionValue() : "기본옵션",
                paymentPrice,
                orderPrice,
                discountPrice,
                quantity,
                paymentPrice * quantity,
                product.getProduct().getMainImage(),
                product.getProduct().getBrand()
        );
    }
}
