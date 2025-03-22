package com.jishop.product.implementation;

import com.jishop.product.domain.Product;
import com.jishop.product.domain.QProduct;
import com.jishop.product.dto.ProductRequest;
import com.jishop.reviewproduct.domain.QReviewProduct;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;

import java.util.List;

public interface ProductQueryHelper {

    BooleanBuilder findProductsByCondition(ProductRequest request, QProduct product, QReviewProduct reviewProduct);

    List<Product> getFilteredAndSortedResults(
            BooleanBuilder filterBuilder, OrderSpecifier<?> orderSpecifier, ProductRequest request);

    long countFilteredProducts(BooleanBuilder filterBuilder);
}
