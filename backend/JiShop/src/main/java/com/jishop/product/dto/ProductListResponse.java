package com.jishop.product.dto;

import com.jishop.product.domain.Labels;
import com.jishop.product.domain.Product;

public record ProductListResponse(
        String name,
        Labels labels,
        Integer originPrice,
        Integer discountPrice,
        Boolean isDiscount,
        String brand,
        String mainImage
) {
    public static ProductListResponse from(Product product) {
        return new ProductListResponse(
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
