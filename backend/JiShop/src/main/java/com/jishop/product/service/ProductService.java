package com.jishop.product.service;

import com.jishop.product.dto.ProductListResponse;
import com.jishop.product.dto.ProductRequest;
import com.jishop.product.dto.ProductResponse;
import org.springframework.data.web.PagedModel;

public interface ProductService {

    PagedModel<ProductListResponse> getProductList(ProductRequest request);

    ProductResponse getProduct(Long id);
}
