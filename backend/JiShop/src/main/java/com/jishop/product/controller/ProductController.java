package com.jishop.product.controller;

import com.jishop.product.dto.ProductResponse;

public interface ProductController {

    ProductResponse getProduct(Long id);
}
