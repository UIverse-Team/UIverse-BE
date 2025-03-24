package com.jishop.category.service;

import com.jishop.category.dto.CategoryResponse;
import org.springframework.data.web.PagedModel;

public interface CategoryService {

    PagedModel<CategoryResponse> getProductsByCategory(Long categoryId, int page);
}
