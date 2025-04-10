package com.jishop.product.repository;

import com.jishop.product.domain.Product;
import com.jishop.product.domain.QProduct;
import com.jishop.product.dto.request.ProductRequest;
import com.jishop.product.implementation.ProductQueryHelper;
import com.jishop.reviewproduct.domain.QReviewProduct;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryQueryDslImpl implements ProductRepositoryQueryDsl {

    private final JPAQueryFactory queryFactory;
    private final ProductQueryHelper productQueryHelper;
    private final QProduct product = QProduct.product;
    private final QReviewProduct reviewProduct = QReviewProduct.reviewProduct;

    @Override
    public List<Product> findProductsByCondition(final ProductRequest productRequest, final int page, final int size) {
        final BooleanBuilder filterBuilder = productQueryHelper
                .findProductsByCondition(productRequest, product, reviewProduct);

        final OrderSpecifier<?> orderSpecifier = addSorting(productRequest.sort(), product);

        return queryFactory.selectFrom(product)
                .where(filterBuilder)
                .orderBy(orderSpecifier)
                .offset((long) page * size)
                .limit(size)
                .fetch();
    }

    @Override
    public long countProductsByCondition(final ProductRequest productRequest) {
        final BooleanBuilder filterBuilder = productQueryHelper
                .findProductsByCondition(productRequest, product, reviewProduct);

        return Optional.ofNullable(
                queryFactory.select(product.count())
                        .from(product)
                        .where(filterBuilder)
                        .fetchOne()
        ).orElse(0L);
    }

    private OrderSpecifier<?> addSorting(final String sort, final QProduct product) {
        return switch (sort) {
            case "latest" -> product.createdAt.desc();
            case "priceAsc" -> product.discountPrice.asc();
            case "priceDesc" -> product.discountPrice.desc();
            case "discount" -> product.discountRate.desc();
            default -> product.wishListCount.desc();
        };
    }
}