package com.jishop.category.controller;

import com.jishop.category.dto.HeaderProductListResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.web.PagedModel;

@Tag(name = "카테고리 API")
public interface CategoryController {

    PagedModel<HeaderProductListResponse> getProductListByCategory(Long CategoryId, int page);
}
