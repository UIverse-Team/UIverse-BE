package com.jishop.category.service;

import com.jishop.category.dto.CategoryResponse;

import java.util.List;

public interface CategoryService {

    List<CategoryResponse> getCategoryFilterInfo();

    List<CategoryResponse> getSubcategoriesByParentId(Long categoryId);
}
