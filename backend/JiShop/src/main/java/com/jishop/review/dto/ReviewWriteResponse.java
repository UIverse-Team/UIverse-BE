package com.jishop.review.dto;

import com.jishop.product.domain.Labels;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReviewWriteResponse(
        Long producId,
        Long orderDetailId,
        String name,
        Labels labels,
        int originPrice,
        boolean isDiscount,
        BigDecimal discountRate,
        int discountPrice,
        String brand,
        LocalDateTime purchaseDate,
        String mainImage,
        int quantity,
        String optionValue) {
}
