package com.jishop.category.dto;

import com.jishop.category.domain.Category;

public record CategoryResponse(
        Long id,
        String name
) {
    public static CategoryResponse from(Category category) {
        return new CategoryResponse(
                category.getCurrentId(),
                category.getName()
        );
    }
}
