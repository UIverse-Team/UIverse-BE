package com.jishop.product.controller;

import com.jishop.product.dto.ProductListRequest;
import com.jishop.product.dto.ProductResponse;
import com.jishop.product.dto.ProductSearchRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.web.PagedModel;

@Tag(name = "상품 API")
public interface ProductController {

    PagedModel<ProductResponse> getProductList(ProductListRequest request);

    ProductResponse getProduct(Long id);

    PagedModel<ProductResponse> searchProducts(ProductSearchRequest request);
}
