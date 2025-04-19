package com.jishop.review.dto;

import com.jishop.review.domain.Review;
import com.jishop.review.domain.tag.Tag;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record MyPageReviewResponse(
        Tag tag,
        int rating,
        String content,
        int likeCount,
        Long productId,
        String image,
        LocalDateTime purchaseDate,
        ProductSummary product,
        String option
) {
    public static MyPageReviewResponse from(Review review) {

        String[] split = review.getProductSummary().split(";");
        String option = null;
        if (split.length == 3) {
            option = split[1];
        }

        return new MyPageReviewResponse(
                review.getTag(),
                review.getRating(),
                review.getContent(),
                review.getLikeCount(),
                review.getProduct().getId(),
                review.getImageUrls().stream().findFirst().orElse(null),
                review.getOrderDetail().getCreatedAt(),
                ProductSummary.from(review.getProduct()),
                option
        );
    }
}

