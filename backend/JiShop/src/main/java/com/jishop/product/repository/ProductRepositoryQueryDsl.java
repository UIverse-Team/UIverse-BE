package com.jishop.product.repository;

import com.jishop.product.domain.Product;
import com.jishop.product.dto.request.ProductRequest;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;

import java.util.List;

public interface ProductRepositoryQueryDsl {

    List<Product> getFilteredAndSortedResults(
            BooleanBuilder filterBuilder, OrderSpecifier<?> orderSpecifier, ProductRequest request, int page, int size);

    long countFilteredProducts(BooleanBuilder filterBuilder);
}
