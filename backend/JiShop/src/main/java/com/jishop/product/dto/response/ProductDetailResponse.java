package com.jishop.product.dto.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.jishop.product.domain.Product;

import java.util.List;
import java.util.stream.Stream;

public record ProductDetailResponse(
        @JsonUnwrapped
        ProductResponse productResponse,
        String description,
        Boolean isDiscount,
        @JsonUnwrapped
        Object option,
        int reviewCount,
        double reviewRate,
        List<String> images,
        String detailImage,
        Object isWished
) {
    public static ProductDetailResponse from(final ProductResponse productResponse, final Product product,
                                             final Boolean isWished, final int reviewCount, final double reviewRate,
                                             final Object option) {
        final Object wishStatus = isWished != null && isWished;

        ProductImageUrlResponse imageUrlResponse = ProductImageUrlResponse.from(product.getImage());

        List<String> imageList = Stream.of(
                        imageUrlResponse.image1(),
                        imageUrlResponse.image2(),
                        imageUrlResponse.image3(),
                        imageUrlResponse.image4()
                )
                .filter(img -> img != null && !img.isEmpty())
                .toList();

        return new ProductDetailResponse(
                productResponse,
                product.getProductInfo().getDescription(),
                product.getStatus().getIsDiscount(),
                option,
                reviewCount,
                reviewRate,
                imageList,
                imageUrlResponse.detailImage(),
                wishStatus
        );
    }
}
