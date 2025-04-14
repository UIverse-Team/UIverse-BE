package com.jishop.category.controller;

import com.jishop.category.dto.CategoryResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "카테고리 API")
public interface CategoryController {

    List<CategoryResponse> getCategoryFilterInfo();

    List<CategoryResponse> getSubcategoriesByParentId(Long categoryId);
}
