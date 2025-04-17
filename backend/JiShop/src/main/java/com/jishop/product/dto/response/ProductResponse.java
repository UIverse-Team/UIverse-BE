package com.jishop.product.dto.response;

import com.jishop.product.domain.Labels;
import com.jishop.product.domain.Product;

public record ProductResponse(
        Long id,
        String name,
        Labels labels,
        Integer originPrice,
        Integer discountPrice,
        String brand,
        int discountRate,
        String mainImage
) {
    public static ProductResponse from(final Product product) {
        return new ProductResponse(
                product.getId(),
                product.getProductInfo().getName(),
                product.getStatus().getLabels(),
                product.getProductInfo().getOriginPrice(),
                product.getProductInfo().getDiscountPrice(),
                product.getProductInfo().getBrand(),
                product.getProductInfo().getDiscountRate(),
                product.getImage().getMainImage()
        );
    }
}
