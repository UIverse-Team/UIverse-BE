package com.jishop.product.service;

import com.jishop.product.dto.response.ProductResponse;
import org.springframework.data.web.PagedModel;

import java.util.List;

public interface ProductCategoryService {

    PagedModel<ProductResponse> getProductsByCategory(final Long categoryId, final int page, final int size);

    List<Long> getCategoryIdsWithSubcategories(final Long categoryId);
}
