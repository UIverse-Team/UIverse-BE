package com.jishop.product.repository;

import com.jishop.product.domain.Product;
import com.jishop.product.dto.request.ProductRequest;

import java.util.List;

public interface ProductRepositoryQueryDsl {

    List<Product> findProductsByCondition(final ProductRequest request,
                                          final int page,
                                          final int size,
                                          final List<Long> categoryIds);

    long countProductsByCondition(final ProductRequest request,  final List<Long> categoryIds);
}
