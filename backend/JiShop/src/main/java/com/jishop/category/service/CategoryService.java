package com.jishop.category.service;

import com.jishop.category.dto.HeaderProductListResponse;
import org.springframework.data.web.PagedModel;

public interface CategoryService {

    PagedModel<HeaderProductListResponse> getProductsByCategory(Long categoryId, int page);
}
