package com.jishop.category.dto;

import com.jishop.product.domain.Product;
import com.jishop.product.dto.ProductListResponse;

import java.util.List;
import java.util.stream.Collectors;

public record CategoryResponse(

        List<ProductListResponse> products
) {
    public static CategoryResponse from(List<Product> products) {
        List<ProductListResponse> productResponses = products.stream()
                .map(ProductListResponse::from).collect(Collectors.toList());

        return new CategoryResponse(productResponses);
    }
}
