package com.jishop.order.dto;

import com.jishop.order.domain.OrderDetail;

public record OrderProductResponse(
        Long id,
        Long saleProductId,
        String mainImage,
        String productName,
        String optionValue,
        int paymentPrice, //결제금액 (원래가격 - 할인가격)
        int orderPrice, // 주문 금액 (원래가격)
        int discountPrice, //할인 가격
        int quantity,
        int totalPrice, //paymentPrice * quantity
        boolean canReview,
        String brandName
) {
    public static OrderProductResponse from(OrderDetail detail, boolean canReview) {
        return new OrderProductResponse(
                detail.getId(),
                detail.getSaleProduct().getId(),
                detail.getSaleProduct().getProduct().getImage().getMainImage(),
                detail.getSaleProduct().getName(),
                detail.getSaleProduct().getOption() != null ? detail.getSaleProduct().getOption().getOptionValue() : null,
                detail.getPaymentPrice(),
                detail.getOrderPrice(),
                detail.getDiscountPrice(),
                detail.getQuantity(),
                detail.getPaymentPrice() * detail.getQuantity(),
                canReview,
                detail.getSaleProduct().getProduct().getProductInfo().getBrand()
        );
    }
}
