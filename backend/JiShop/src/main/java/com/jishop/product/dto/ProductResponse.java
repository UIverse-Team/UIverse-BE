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
        String detailImage,
        // true 또는 빈배열 반환
        Object isWished
) {
    public static ProductResponse from(Product product, Boolean isWished) {
        Object wishStatus = isWished != null && isWished ? true : new String[0];

        return new ProductResponse(
                product.getName(),
                product.getLabels(),
                product.getDescription(),
                product.getOriginPrice(),
                product.getDiscountPrice(),
                product.getIsDiscount(),
                product.getBrand(),
                new String[] {
                        product.getMainImage(), product.getImage1(), product.getImage2(), product.getImage3(), product.getImage4()
                },
                product.getDetailImage(),
                wishStatus
        );
    }
}
