package com.jishop.product.repository;

import com.jishop.product.domain.Product;
import com.jishop.product.domain.QProduct;
import com.jishop.product.dto.request.ProductRequest;
import com.jishop.reviewproduct.domain.QReviewProduct;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
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
        final BooleanBuilder builder = new BooleanBuilder();

        if (productRequest.priceRanges() != null && !productRequest.priceRanges().isEmpty()) {
            addPriceRangesFiltering(productRequest.priceRanges(), builder);
        }
        if (productRequest.ratings() != null && !productRequest.ratings().isEmpty()) {
            addRatingsFilter(productRequest.ratings(), builder);
        }
        if (categoryIds != null && !categoryIds.isEmpty()) {
            addCategoryFilter(categoryIds, builder);
        }
        if (productRequest.keyword() != null && !productRequest.keyword().trim().isEmpty()) {
            addKeywordFilter(productRequest.keyword(), builder);
        }

        final OrderSpecifier<?> orderSpecifier = addSorting(productRequest.sort());

//        BooleanBuilder testBuilder = new BooleanBuilder();
//        testBuilder.and(reviewProduct.reviewCount.gt(0));
//
//        JPQLQuery<Long> simpleSubQuery = JPAExpressions
//                .select(reviewProduct.product.id)
//                .from(reviewProduct)
//                .where(testBuilder);
//
//        builder.and(product.id.in(simpleSubQuery));

        return queryFactory.selectFrom(product)
                .where(builder)
                .orderBy(orderSpecifier)
                .offset((long) page * size)
                .limit(size)
                .fetch();
    }

    @Override
    public long countProductsByCondition(final ProductRequest productRequest, final List<Long> categoryIds) {
        final BooleanBuilder builder = new BooleanBuilder();

        if (productRequest.priceRanges() != null && !productRequest.priceRanges().isEmpty()) {
            addPriceRangesFiltering(productRequest.priceRanges(), builder);
        }
        if (productRequest.ratings() != null && !productRequest.ratings().isEmpty()) {
            addRatingsFilter(productRequest.ratings(), builder);
        }
        if (categoryIds != null && !categoryIds.isEmpty()) {
            addCategoryFilter(categoryIds, builder);
        }
        if (productRequest.keyword() != null && !productRequest.keyword().trim().isEmpty()) {
            addKeywordFilter(productRequest.keyword(), builder);
        }

        return Optional.ofNullable(
                queryFactory.select(product.count())
                        .from(product)
                        .where(builder)
                        .fetchOne()
        ).orElse(0L);
    }

    private OrderSpecifier<?> addSorting(final String sort) {
        return switch (sort) {
            case "latest" -> product.createdAt.desc();
            case "priceAsc" -> product.productInfo.discountPrice.asc();
            case "priceDesc" -> product.productInfo.discountPrice.desc();
            case "discount" -> product.productInfo.discountRate.desc();
            default -> product.wishListCount.desc();
        };
    }

    private void addPriceRangesFiltering(final List<Integer> priceRanges, final BooleanBuilder builder) {
        final BooleanBuilder priceBuilder = new BooleanBuilder();

        for (final Integer range : priceRanges) {
            switch (range) {
                case 0 ->
                        priceBuilder.or(productInfo.discountPrice.between(0, 25000));
                case 25000 ->
                        priceBuilder.or(productInfo.discountPrice.between(25001, 50000));
                case 50000 ->
                        priceBuilder.or(productInfo.discountPrice.between(50001, 100000));
                case 100000 ->
                        priceBuilder.or(productInfo.discountPrice.gt(100000));
                default -> { }
            }
        }

        if (priceBuilder.hasValue()) {
            builder.and(priceBuilder);
        }
    }

    private static void addRatingsFilter(final List<Integer> ratings, final BooleanBuilder builder) {
        if (ratings.size() != 5 ||
                !ratings.contains(1) ||
                !ratings.contains(2) ||
                !ratings.contains(3) ||
                !ratings.contains(4) ||
                !ratings.contains(5)) {

            final BooleanBuilder ratingBuilder = new BooleanBuilder();

            // review_count > 0 조건을 AND로 추가 (OR 대신)
            ratingBuilder.and(reviewProduct.reviewCount.gt(0));

            // 평점 필터링을 위한 내부 Builder
            BooleanBuilder ratingRangeBuilder = new BooleanBuilder();

            NumberExpression<Double> avgRating = Expressions.numberTemplate(
                    Double.class,
                    "1.0 * {0} / {1}",
                    reviewProduct.reviewScore,
                    reviewProduct.reviewCount
            );

            // 평점 = 리뷰총점 / 리뷰개수
            for (final Integer rating : ratings) {
                switch (rating) {
                    case 1 -> ratingRangeBuilder.or(
                            avgRating.goe(1.0).and(avgRating.lt(2.0))
                    );
                    case 2 -> ratingRangeBuilder.or(
                            avgRating.goe(2.0).and(avgRating.lt(3.0))
                    );
                    case 3 -> ratingRangeBuilder.or(
                            avgRating.goe(3.0).and(avgRating.lt(4.0))
                    );
                    case 4 -> ratingRangeBuilder.or(
                            avgRating.goe(4.0).and(avgRating.lt(5.0))
                    );
                    case 5 -> ratingRangeBuilder.or(
                            avgRating.goe(5.0)
                    );
                    default -> {}
                }
            }

            // 평점 범위 조건을 메인 rating builder에 AND로 추가
            if (ratingRangeBuilder.hasValue()) {
                ratingBuilder.and(ratingRangeBuilder);
            }

            if (ratingBuilder.hasValue()) {
                final JPQLQuery<Long> subQuery = JPAExpressions
                        .select(reviewProduct.product.id)
                        .from(reviewProduct)
                        .where(ratingBuilder);
                builder.and(product.id.in(subQuery));
            }
        }
    }

    private void addCategoryFilter(final List<Long> categoryIds, final BooleanBuilder builder) {
        builder.and(product.category.id.in(categoryIds));
    }

    private static void addKeywordFilter(final String keyword, final BooleanBuilder builder) {
        builder.and(
                product.productInfo.name.containsIgnoreCase(keyword)
                        .or(product.productInfo.description.containsIgnoreCase(keyword))
        );
    }
}