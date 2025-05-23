package com.jishop.product.implementation;

import com.jishop.product.domain.QProduct;
import com.jishop.product.dto.request.ProductRequest;
import com.jishop.reviewproduct.domain.QReviewProduct;
import com.querydsl.core.BooleanBuilder;

public interface ProductQueryHelper {

    BooleanBuilder findProductsByCondition(final ProductRequest request,
                                           final QProduct product, final QReviewProduct reviewProduct);
}
