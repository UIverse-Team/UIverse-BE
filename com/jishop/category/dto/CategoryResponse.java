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
    // 단일 제품을 처리하는 기존 메서드
    public static CategoryResponse from(
            Long categoryId,
            String categoryName,
            List<SubCategory> subCategories,
            Product product
    ) {
        return new CategoryResponse(
                categoryId,
                categoryName,
                subCategories,
                List.of(ProductListResponse.from(product))
        );
    }
    
    // 제품 리스트를 처리하는 새로운 메서드 추가
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
