package com.jishop.product.repository;

import com.jishop.product.domain.Product;
import com.jishop.product.dto.request.ProductRequest;

import java.util.List;

public interface ProductRepositoryQueryDsl {

    List<Product> findProductsByCondition(ProductRequest request, int page, int size);

    long countProductsByCondition(ProductRequest request);
}
