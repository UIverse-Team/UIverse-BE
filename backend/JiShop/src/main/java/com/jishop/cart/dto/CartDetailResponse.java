package com.jishop.cart.dto;

import com.jishop.cart.domain.Cart;
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
        String brandName,
        boolean isExisted //장바구니에 동일상품이 존재하는지
) {
    public static CartDetailResponse from(
            Long cartId,
            SaleProduct product,
            int quantity,
            int paymentPrice,
            int orderPrice,
            int discountPrice,
            boolean isExisted
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
                product.getProduct().getBrand(),
                isExisted
        );
    }

    public static CartDetailResponse of(Cart cart, boolean isExisted) {
        SaleProduct saleProduct = cart.getSaleProduct();
        int paymentPrice = saleProduct.getProduct().getDiscountPrice();
        int orderPrice = saleProduct.getProduct().getOriginPrice();
        int discountPrice = orderPrice - paymentPrice;

        return new CartDetailResponse(
                cart.getId(),
                saleProduct.getId(),
                saleProduct.getProduct().getName(),
                saleProduct.getOption() != null ? saleProduct.getOption().getOptionValue() : "기본옵션",
                paymentPrice,
                orderPrice,
                discountPrice,
                cart.getQuantity(),
                paymentPrice * cart.getQuantity(),
                saleProduct.getProduct().getMainImage(),
                saleProduct.getProduct().getBrand(),
                isExisted
        );
    }
}