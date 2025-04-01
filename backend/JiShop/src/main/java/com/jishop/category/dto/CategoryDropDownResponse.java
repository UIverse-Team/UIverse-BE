package com.jishop.category.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.List;

public record CategoryDropDownResponse(
        @JsonUnwrapped
        CategoryResponse categoryResponse,
        List<CategoryResponse> subCategories
) {
    public static CategoryDropDownResponse from(
            CategoryResponse categoryResponse,
            List<CategoryResponse> subCategories
    ) {
        return new CategoryDropDownResponse(
                categoryResponse,
                subCategories
        );
    }
}
