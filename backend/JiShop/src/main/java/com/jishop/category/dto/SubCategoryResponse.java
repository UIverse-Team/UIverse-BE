package com.jishop.category.dto;

public record SubCategoryResponse(
        Long id,
        String name,
        long productCount
) {
}
