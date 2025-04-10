package com.jishop.product.dto.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.jishop.product.domain.Product;

public record ProductDetailResponse(
        @JsonUnwrapped
        ProductResponse productResponse,
        String description,
        Boolean isDiscount,
        @JsonUnwrapped
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
                product.getProductInfo().getDescription(),
                product.getStatus().getIsDiscount(),
                option,
                reviewCount,
                reviewRate,
                new String[] {
                        product.getImage().getImage1(),
                        product.getImage().getImage2(),
                        product.getImage().getImage3(),
                        product.getImage().getImage4()
                },
                product.getImage().getDetailImage(),
                wishStatus
        );
    }
}
