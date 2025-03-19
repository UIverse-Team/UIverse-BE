package com.jishop.order.dto;

public record OrderDetailResponse(
        Long id,
        Long saleProductId,
        String productName,
        String optionValue,
        int paymentPrice,
        int orderPrice,
        int discountPrice,
        int quantity,
        int totalPrice,
        boolean canReview
) {
}