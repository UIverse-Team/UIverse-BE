package com.jishop.product.controller;

import com.jishop.product.dto.ProductRequest;
import com.jishop.product.dto.ProductResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.web.PagedModel;

@Tag(name = "상품 API")
public interface ProductController {

    PagedModel<ProductResponse> getProductList(ProductRequest productRequest);

    ProductResponse getProduct(Long id);
}
