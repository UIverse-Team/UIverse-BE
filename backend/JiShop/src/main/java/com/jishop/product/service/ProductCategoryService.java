package com.jishop.product.service;

import com.jishop.product.dto.response.ProductResponse;
import org.springframework.data.web.PagedModel;

public interface ProductCategoryService {

    PagedModel<ProductResponse> getProductsByCategory(final Long categoryId, final int page, final int size);
}
