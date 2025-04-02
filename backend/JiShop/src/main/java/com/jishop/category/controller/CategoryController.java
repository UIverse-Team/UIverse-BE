package com.jishop.category.controller;

import com.jishop.category.dto.CategoryResponse;
import com.jishop.product.dto.response.ProductResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.web.PagedModel;

import java.util.List;

@Tag(name = "카테고리 API")
public interface CategoryController {

    PagedModel<ProductResponse> getProductListByCategory(Long CategoryId, int page);

    List<CategoryResponse> getCategoryFilterInfo();

    List<CategoryResponse> getSubcategoriesByParentId(Long categoryId);
}
