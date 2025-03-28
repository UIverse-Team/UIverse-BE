package com.jishop.category.dto;

import com.jishop.product.domain.Product;
import com.jishop.product.dto.response.ProductListResponse;

import java.util.List;

public record HeaderProductListResponse(
        CategoryResponse categoryResponse,
        List<SubCategoryResponse> subCategories,
        List<ProductListResponse> products
) {
    public static HeaderProductListResponse from(
            CategoryResponse categoryResponse,
            List<SubCategoryResponse> subCategories,
            List<Product> products
    ) {
        List<ProductListResponse> productResponses = products.stream()
                .map(ProductListResponse::from)
                .toList();

        return new HeaderProductListResponse(
                categoryResponse,
                subCategories,
                productResponses
        );
    }
}
