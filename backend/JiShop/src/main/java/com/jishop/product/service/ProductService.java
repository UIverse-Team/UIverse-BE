package com.jishop.product.service;

import com.jishop.product.dto.GetProductResponse;

public interface ProductService {

    GetProductResponse getProductList(Long id);
}
