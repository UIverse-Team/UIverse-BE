package com.jishop.review.dto;

import com.jishop.product.domain.Labels;

import java.time.LocalDateTime;

public record ReviewWriteResponse(
        Long producId,
        Long orderDetailId,
        String name,
        Labels labels,
        int originPrice,
        boolean isDiscount,
        int discountRate,
        int discountPrice,
        String brand,
        LocalDateTime purchaseDate,
        String mainImage,
        int quantity,
        String optionValue) {
}
