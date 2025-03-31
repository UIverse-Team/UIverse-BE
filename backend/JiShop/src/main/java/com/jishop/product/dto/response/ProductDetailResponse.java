package com.jishop.product.dto.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.jishop.product.domain.Product;

public record ProductDetailResponse(
        @JsonUnwrapped
        ProductResponse productResponse,
        String description,
        Boolean isDiscount,
        Object option,
        int reviewCount,
        double reviewRate,
        String[] images,
        String detailImage,
        Object isWished
) {
    public static ProductDetailResponse from(final ProductResponse productResponse, final Product product,
                                             final Boolean isWished, final int reviewCount, final double reviewRate,
                                             final Object option) {
        final Object wishStatus = isWished != null && isWished;

        return new ProductDetailResponse(
                productResponse,
                product.getDescription(),
                product.getIsDiscount(),
                option,
                reviewCount,
                reviewRate,
                new String[] { product.getImage1(), product.getImage2(), product.getImage3(), product.getImage4() },
                product.getDetailImage(),
                wishStatus
        );
    }
}
