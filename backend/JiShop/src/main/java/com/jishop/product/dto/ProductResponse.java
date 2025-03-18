package com.jishop.product.dto;

import com.jishop.product.domain.Labels;
import com.jishop.product.domain.Product;

public record ProductResponse(
        String name,
        Labels labels,
        String description,
        Integer originPrice,
        Integer discountPrice,
        Boolean isDiscount,
        String brand,
        String[] images,
        String detailImage
) {
    public static ProductResponse from(Product product) {
        String[] images = new String[] {
                product.getMainImage(),
                product.getImage1(),
                product.getImage2(),
                product.getImage3(),
                product.getImage4()
        };

        return new ProductResponse(
                product.getName(),
                product.getLabels(),
                product.getDescription(),
                product.getOriginPrice(),
                product.getDiscountPrice(),
                product.getIsDiscount(),
                product.getBrand(),
                images,
                product.getDetailImage()
        );
    }
}
