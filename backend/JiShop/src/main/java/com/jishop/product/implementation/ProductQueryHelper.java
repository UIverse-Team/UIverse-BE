package com.jishop.product.implementation;

import com.jishop.product.domain.QProduct;
import com.jishop.product.dto.ProductRequest;
import com.jishop.reviewproduct.domain.QReviewProduct;
import com.querydsl.core.BooleanBuilder;

public interface ProductQueryHelper {

    BooleanBuilder findProductsByCondition(ProductRequest request, QProduct product, QReviewProduct reviewProduct);
}
