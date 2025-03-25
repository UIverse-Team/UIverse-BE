package com.jishop.product.dto.response;

import com.jishop.product.domain.Labels;
import com.jishop.product.domain.Product;

public record ProductResponse(
        Long id,
        String name,
        Labels labels,
        String description,
        Integer originPrice,
        Integer discountPrice,
        Boolean isDiscount,
        String brand,
        Object option,
        int reviewCount,
        double reviewRate,
        String[] images,
        String detailImage,
        // true 또는 빈배열 반환
        Object isWished
) {
    public static ProductResponse from(Product product, Boolean isWished, int reviewCount, double reviewRate, Object option) {
        Object wishStatus = isWished != null && isWished ? true : new String[0];

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getLabels(),
                product.getDescription(),
                product.getOriginPrice(),
                product.getDiscountPrice(),
                product.getIsDiscount(),
                product.getBrand(),
                option,
                reviewCount,
                reviewRate,
                new String[] {
                        product.getMainImage(), product.getImage1(), product.getImage2(), product.getImage3(), product.getImage4()
                },
                product.getDetailImage(),
                wishStatus
        );
    }
}
