package com.jishop.product.repository;

import com.jishop.product.domain.Product;
import com.jishop.product.dto.ProductRequest;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;

import java.util.List;

public interface ProductRepositoryQueryDsl {

    List<Product> getFilteredAndSortedResults(
            BooleanBuilder filterBuilder, OrderSpecifier<?> orderSpecifier, ProductRequest request);

    long countFilteredProducts(BooleanBuilder filterBuilder);
}
