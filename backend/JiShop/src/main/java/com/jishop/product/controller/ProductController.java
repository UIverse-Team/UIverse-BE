package com.jishop.product.controller;

import com.jishop.product.dto.ProductRequest;
import com.jishop.product.dto.ProductResponse;
import org.springframework.data.web.PagedModel;

public interface ProductController {

    ProductResponse getProduct(Long id);

    PagedModel<ProductResponse> getProductList(ProductRequest productRequest);
}
