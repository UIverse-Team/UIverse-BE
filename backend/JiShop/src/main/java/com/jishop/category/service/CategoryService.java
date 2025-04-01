package com.jishop.category.service;

import com.jishop.category.dto.CategoryResponse;
import com.jishop.product.dto.response.ProductResponse;
import org.springframework.data.web.PagedModel;

import java.util.List;

public interface CategoryService {

    PagedModel<ProductResponse> getProductsByCategory(Long categoryId, int page);

    List<CategoryResponse> getCategoryFilterInfo();

    List<CategoryResponse> getSubcategoriesByParentId(Long categoryId);
}
