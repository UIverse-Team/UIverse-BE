package com.jishop.order.dto;

import java.time.LocalDateTime;

public record OrderDetailResponse(
        Long id,
        String orderNumber,
        Long saleProductId,
        String productName,
        String optionValue,
        int paymentPrice,
        int orderPrice,
        int discountPrice,
        int quantity,
        int totalPrice,
        boolean canReview,
        String brand,
        LocalDateTime createdAt,
        String receiver,
        String receiverNumber,
        String zipCode,
        String baseAddress,
        String detailAddress,
        String email
) {
}