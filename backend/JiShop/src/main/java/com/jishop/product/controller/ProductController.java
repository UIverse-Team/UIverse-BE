package com.jishop.product.controller;

import com.jishop.product.dto.GetProductResponse;

public interface ProductController {

    GetProductResponse getProduct(Long id);
}
