package com.jishop.product.implementation;

import com.jishop.product.domain.QProduct;
import com.jishop.product.dto.request.ProductRequest;
import com.jishop.reviewproduct.domain.QReviewProduct;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductQueryHelperImpl implements ProductQueryHelper {

    @Override
    public BooleanBuilder findProductsByCondition(
            ProductRequest productRequest, QProduct product, QReviewProduct reviewProduct) {
        var builder = new BooleanBuilder();

        addPriceRangesFiltering(productRequest.priceRanges(), product, builder);
        addRatingsFilter(productRequest.ratings(), reviewProduct, product, builder);
        addCategory(productRequest.category(), product, builder);
        addKeyword(productRequest.keyword(), product, builder);

        return builder;
    }

    private static void addPriceRangesFiltering(List<Integer> priceRanges, QProduct product, BooleanBuilder builder) {
        var priceBuilder = new BooleanBuilder();

        for (Integer range : priceRanges) {
            switch (range) {
                case 0 ->
                        priceBuilder.or(product.discountPrice.between(0, 25000));
                case 25000 ->
                        priceBuilder.or(product.discountPrice.between(25001, 50000));
                case 50000 ->
                        priceBuilder.or(product.discountPrice.between(50001, 100000));
                case 100000 ->
                        priceBuilder.or(product.discountPrice.gt(100000));
                default -> { }
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

            var ratingBuilder = new BooleanBuilder();

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
