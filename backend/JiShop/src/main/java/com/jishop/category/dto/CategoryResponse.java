package com.jishop.category.dto;

import com.jishop.product.domain.Product;
import com.jishop.product.dto.response.ProductListResponse;

import java.util.List;

public record CategoryResponse(
        Long categoryId,
        String categoryName,
        List<SubCategory> subCategories,
        List<ProductListResponse> products
) {
    public static CategoryResponse from(
            Long categoryId,
            String categoryName,
            List<SubCategory> subCategories,
            List<Product> products
    ) {
        List<ProductListResponse> productResponses = products.stream()
                .map(ProductListResponse::from)
                .toList();

        return new CategoryResponse(
                categoryId,
                categoryName,
                subCategories,
                productResponses
        );
    }
}
