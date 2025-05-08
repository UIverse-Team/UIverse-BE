package com.jishop.product.service;

import com.jishop.common.response.PageResponse;
import com.jishop.product.dto.response.ProductResponse;

import java.util.List;

public interface ProductCategoryService {

    PageResponse<ProductResponse> getProductsByCategory(final Long categoryId, final int page, final int size);

    List<Long> getCategoryIdsWithSubcategories(final Long categoryId);
}
