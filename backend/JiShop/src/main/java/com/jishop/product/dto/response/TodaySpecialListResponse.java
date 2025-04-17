package com.jishop.product.dto.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.jishop.product.domain.Product;

public record TodaySpecialListResponse(
        @JsonUnwrapped
        ProductResponse productResponse,
        long totalSales
) {
    public static TodaySpecialListResponse from(final Product product, final long totalSales) {
        return new TodaySpecialListResponse(
                ProductResponse.from(product),
                totalSales
        );
    }
}
