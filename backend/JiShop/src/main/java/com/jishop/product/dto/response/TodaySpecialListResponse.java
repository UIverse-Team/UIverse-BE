package com.jishop.product.dto.response;

import com.jishop.product.domain.Product;

public record TodaySpecialListResponse(
        ProductListResponse productListResponse,
        long totalSales
) {
    public static TodaySpecialListResponse from(final Product product, final long totalSales) {
        return new TodaySpecialListResponse(
                ProductListResponse.from(product),
                totalSales
        );
    }
}
