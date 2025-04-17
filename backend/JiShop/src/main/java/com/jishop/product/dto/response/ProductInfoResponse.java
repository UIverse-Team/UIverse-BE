package com.jishop.product.dto.response;

import com.jishop.product.domain.embed.ProductInfo;

public record ProductInfoResponse(
        String name,
        String brand,
        String description,
        int originPrice,
        int discountPrice,
        int discountRate
) {
    public static ProductInfoResponse from(ProductInfo productInfo) {
        return new ProductInfoResponse(
                productInfo.getName(),
                productInfo.getBrand(),
                productInfo.getDescription(),
                productInfo.getOriginPrice(),
                productInfo.getDiscountPrice(),
                productInfo.getDiscountRate()
        );
    }
}