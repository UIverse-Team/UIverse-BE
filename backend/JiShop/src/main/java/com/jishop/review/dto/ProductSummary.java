package com.jishop.review.dto;

import com.jishop.product.domain.Labels;
import com.jishop.product.domain.Product;

public record ProductSummary(
        Long id,
        String name,
        Labels labels,
        int originPrice,
        int discountPrice,
        boolean isDiscount,
        String brand,
        String mainImage
) { 
    public static ProductSummary from(Product product) {
        return new ProductSummary(
                product.getId(),
                product.getName(),
                product.getLabels(),
                product.getOriginPrice(),
                product.getDiscountPrice(),
                product.getIsDiscount(),
                product.getBrand(),
                product.getMainImage()
        );
    }
}
