package com.jishop.category.controller;

import com.jishop.category.dto.CategoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "카테고리 API")
public interface CategoryController {

    @Operation(summary = "최상위 카테고리의 ID와 이름 조회")
    List<CategoryResponse> getCategoryFilterInfo();

    @Operation(summary = "해당 카테고리의 한단계 하위카테고리 ID 조회")
    List<CategoryResponse> getSubcategoriesByParentId(Long categoryId);
}
