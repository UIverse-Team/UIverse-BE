package com.jishop.product.dto.response;

import com.jishop.product.domain.Labels;
import com.jishop.product.domain.Product;

public record ProductListResponse(

        Long id,
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
