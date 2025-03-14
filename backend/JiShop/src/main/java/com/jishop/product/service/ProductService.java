package com.jishop.product.service;

import com.jishop.product.dto.ProductResponse;

public interface ProductService {

    ProductResponse getProduct(Long id);
}
