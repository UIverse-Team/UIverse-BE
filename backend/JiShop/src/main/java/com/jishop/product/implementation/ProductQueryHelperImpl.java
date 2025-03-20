package com.jishop.product.implementation;

import com.jishop.product.domain.Product;
import com.jishop.product.domain.QProduct;
import com.jishop.product.dto.ProductRequest;
import com.jishop.reviewproduct.domain.QReviewProduct;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductQueryHelperImpl implements ProductQueryHelper {

    private final JPAQueryFactory queryFactory;

    @Override
    public BooleanBuilder findProductsByCondition(ProductRequest request, QProduct product, QReviewProduct reviewProduct) {

        BooleanBuilder builder = new BooleanBuilder();

        addPriceRangesFiltering(request.priceRanges(), product, builder);
        addRatingsFilter(request.ratings(), reviewProduct, product, builder);
        addCategory(request.category(), product, builder);
        addKeyword(request.keyword(), product, builder);

        return builder;
    }

    @Override
    public List<Product> getFilteredAndSortedResults(
            BooleanBuilder filterBuilder, OrderSpecifier<?> orderSpecifier, ProductRequest request) {

        QProduct product = QProduct.product;

        return queryFactory.selectFrom(product)
                .where(filterBuilder)
                .orderBy(orderSpecifier)
                .offset(request.page() * request.size())
                .limit(request.size())
                .fetch();
    }

    @Override
    public long countFilteredProducts(BooleanBuilder filterBuilder) {
        QProduct product = QProduct.product;
        return queryFactory.selectFrom(product)
                .where(filterBuilder)
                .fetchCount();
    }

    private static void addPriceRangesFiltering(List<String> priceRanges, QProduct product, BooleanBuilder builder) {

        BooleanBuilder priceBuilder = new BooleanBuilder();

        for (String range : priceRanges) {
            switch (range) {
                case "0-25000" -> priceBuilder.or(product.discountPrice.between(0, 25000));
                case "25000-50000" -> priceBuilder.or(product.discountPrice.between(25001, 50000));
                case "50000-100000" -> priceBuilder.or(product.discountPrice.between(50001, 100000));
                case "100000+" -> priceBuilder.or(product.discountPrice.gt(100000));
                default -> {
                }
            }
        }

        if (priceBuilder.hasValue()) {
            builder.and(priceBuilder);
        }
    }

    private static void addRatingsFilter(
            List<Integer> ratings, QReviewProduct reviewProduct, QProduct product, BooleanBuilder builder) {

        if (ratings.size() != 5 ||
                !ratings.contains(1) ||
                !ratings.contains(2) ||
                !ratings.contains(3) ||
                !ratings.contains(4) ||
                !ratings.contains(5)) {

            BooleanBuilder ratingBuilder = new BooleanBuilder();

            // 나눗셈 오류 방지
            ratingBuilder.and(reviewProduct.reviewCount.gt(0));

            // 평점 = 리뷰총점 / 리뷰개수
            for (Integer rating : ratings) {
                switch (rating) {
                    case 1 -> ratingBuilder.or(
                            reviewProduct.reviewScore.divide(reviewProduct.reviewCount)
                                    .goe(1.0).and(
                                            reviewProduct.reviewScore.divide(reviewProduct.reviewCount).lt(2.0)
                                    )
                    );
                    case 2 -> ratingBuilder.or(
                            reviewProduct.reviewScore.divide(reviewProduct.reviewCount)
                                    .goe(2.0).and(
                                            reviewProduct.reviewScore.divide(reviewProduct.reviewCount).lt(3.0)
                                    )
                    );
                    case 3 -> ratingBuilder.or(
                            reviewProduct.reviewScore.divide(reviewProduct.reviewCount)
                                    .goe(3.0).and(
                                            reviewProduct.reviewScore.divide(reviewProduct.reviewCount).lt(4.0)
                                    )
                    );
                    case 4 -> ratingBuilder.or(
                            reviewProduct.reviewScore.divide(reviewProduct.reviewCount)
                                    .goe(4.0).and(
                                            reviewProduct.reviewScore.divide(reviewProduct.reviewCount).lt(5.0)
                                    )
                    );
                    case 5 -> ratingBuilder.or(
                            reviewProduct.reviewScore.divide(reviewProduct.reviewCount).goe(5.0)
                    );
                    default -> {}
                }
            }

            if (ratingBuilder.hasValue()) {
                JPQLQuery<Long> subQuery = JPAExpressions
                        .select(reviewProduct.product.id)
                        .from(reviewProduct)
                        .where(ratingBuilder);
                builder.and(product.id.in(subQuery));

                // 아래는 직접 JOIN 방식 (메인 쿼리에 JOIN 조건 추가 필요)
                // queryFactory.selectFrom(product)
                //     .join(reviewProduct).on(product.id.eq(reviewProduct.productId))
                //     .where(builder.and(ratingBuilder))
            }
        }
    }

    private static void addCategory(Long category, QProduct product, BooleanBuilder builder) {
//        builder.andAnyOf(
//        );
    }

    private static void addKeyword(String keyword, QProduct product, BooleanBuilder builder) {
        builder.and(
                product.name.containsIgnoreCase(keyword).or(product.description.containsIgnoreCase(keyword))
        );
    }
}
