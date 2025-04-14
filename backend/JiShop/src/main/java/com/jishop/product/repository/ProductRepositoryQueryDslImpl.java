package com.jishop.product.repository;

import com.jishop.product.domain.Product;
import com.jishop.product.domain.QProduct;
import com.jishop.product.dto.request.ProductRequest;
import com.jishop.reviewproduct.domain.QReviewProduct;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.jishop.product.domain.embed.QProductInfo.productInfo;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryQueryDslImpl implements ProductRepositoryQueryDsl {

    private final JPAQueryFactory queryFactory;
    private static final QProduct product = QProduct.product;
    private static final QReviewProduct reviewProduct = QReviewProduct.reviewProduct;

    @Override
    public List<Product> findProductsByCondition(final ProductRequest productRequest,
                                                 final int page,
                                                 final int size,
                                                 final List<Long> categoryIds) {

        return queryFactory.selectFrom(product)
                .where(
                        priceRangesFilter(productRequest.priceRanges()),
                        ratingsFilter(productRequest.ratings()),
                        categoryFilter(categoryIds),
                        keywordFilter(productRequest.keyword())
                )
                .orderBy(getSortOrderSpecifier(productRequest.sort()))
                .offset((long) page * size)
                .limit(size)
                .fetch();
    }

    @Override
    public long countProductsByCondition(final ProductRequest productRequest, final List<Long> categoryIds) {

        return Optional.ofNullable(
                queryFactory.select(product.count())
                        .from(product)
                        .where(
                                priceRangesFilter(productRequest.priceRanges()),
                                ratingsFilter(productRequest.ratings()),
                                categoryFilter(categoryIds),
                                keywordFilter(productRequest.keyword())
                        )
                        .fetchOne()
        ).orElse(0L);
    }

    private OrderSpecifier<?> getSortOrderSpecifier(final String sort) {
        return switch (sort) {
            case "latest" -> product.createdAt.desc();
            case "priceAsc" -> product.productInfo.discountPrice.asc();
            case "priceDesc" -> product.productInfo.discountPrice.desc();
            case "discount" -> product.productInfo.discountRate.desc();
            default -> product.wishListCount.desc();
        };
    }

    private BooleanExpression priceRangesFilter(final List<Integer> priceRanges) {
        BooleanBuilder priceBuilder = new BooleanBuilder();

        for (final Integer range : priceRanges) {
            switch (range) {
                case 0 -> priceBuilder.or(productInfo.discountPrice.between(0, 25000));
                case 25000 -> priceBuilder.or(productInfo.discountPrice.between(25001, 50000));
                case 50000 -> priceBuilder.or(productInfo.discountPrice.between(50001, 100000));
                case 100000 -> priceBuilder.or(productInfo.discountPrice.gt(100000));
                default -> {}
            }
        }

        return priceBuilder.hasValue() ? Expressions.asBoolean(priceBuilder.getValue()) : null;
    }

    private BooleanExpression ratingsFilter(final List<Integer> ratings) {
        if (ratings.size() == 5) {
            return null;
        }
        // 카운트 > 0 조건 builder
        BooleanBuilder ratingBuilder = new BooleanBuilder();
        ratingBuilder.and(reviewProduct.reviewCount.gt(0));

        NumberExpression<Double> avgRating = Expressions.numberTemplate(
                Double.class,
                "1.0 * {0} / {1}",
                reviewProduct.reviewScore,
                reviewProduct.reviewCount
        );

        // 평점 범위 조건 builder
        BooleanBuilder ratingRangeBuilder = new BooleanBuilder();
        for (final Integer rating : ratings) {
            switch (rating) {
                case 1 -> ratingRangeBuilder.or(avgRating.goe(1.0).and(avgRating.lt(2.0)));
                case 2 -> ratingRangeBuilder.or(avgRating.goe(2.0).and(avgRating.lt(3.0)));
                case 3 -> ratingRangeBuilder.or(avgRating.goe(3.0).and(avgRating.lt(4.0)));
                case 4 -> ratingRangeBuilder.or(avgRating.goe(4.0).and(avgRating.lt(5.0)));
                case 5 -> ratingRangeBuilder.or(avgRating.goe(5.0));
                default -> {
                }
            }
        }
        if (ratingRangeBuilder.hasValue()) {
            ratingBuilder.and(ratingRangeBuilder);
        }
        return ratingBuilder.hasValue() ?
                product.id.in(
                        JPAExpressions.select(reviewProduct.product.id)
                                .from(reviewProduct)
                                .where(ratingBuilder)
                ) : null;
    }

    private BooleanExpression categoryFilter(final List<Long> categoryIds) {

        return product.category.id.in(categoryIds);
    }

    private BooleanExpression keywordFilter(final String keyword) {

        return product.productInfo.name.containsIgnoreCase(keyword)
                .or(product.productInfo.description.containsIgnoreCase(keyword));
    }
}