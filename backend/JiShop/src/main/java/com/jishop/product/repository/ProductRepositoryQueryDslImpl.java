package com.jishop.product.repository;

import com.jishop.product.domain.Product;
import com.jishop.product.domain.QProduct;
import com.jishop.product.dto.request.ProductRequest;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryQueryDslImpl implements ProductRepositoryQueryDsl{

    private final JPAQueryFactory queryFactory;
    private final QProduct product = QProduct.product;

    @Override
    public List<Product> getFilteredAndSortedResults(
            BooleanBuilder filterBuilder, OrderSpecifier<?> orderSpecifier, ProductRequest productRequest) {

        return queryFactory.selectFrom(product)
                .where(filterBuilder)
                .orderBy(orderSpecifier)
                .offset((long) productRequest.page() * productRequest.size())
                .limit(productRequest.size())
                .fetch();
    }

    @Override
    public long countFilteredProducts(BooleanBuilder filterBuilder) {
        return queryFactory.selectFrom(product)
                .where(filterBuilder)
                .fetchCount();
    }
}
