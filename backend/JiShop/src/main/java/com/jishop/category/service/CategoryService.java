package com.jishop.category.service;

import com.jishop.category.dto.CategoryResponse;

import java.util.List;

public interface CategoryService {

//    PagedModel<ProductResponse> getProductsByCategory(Long categoryId, int page);

    List<CategoryResponse> getCategoryFilterInfo();

    List<CategoryResponse> getSubcategoriesByParentId(Long categoryId);
}
