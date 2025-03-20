package com.jishop.review.dto;

import com.jishop.member.domain.User;
import com.jishop.review.domain.Review;
import com.jishop.review.domain.tag.Tag;

import java.time.LocalDate;
import java.util.List;

public record MyPageReviewResponse(
        Tag tag,
        int rating,
        String content,
        int likeCount,
        Long productId,
        List<String> images,
        LocalDate createAt,
        String option,
        String userName
) {
    public static MyPageReviewResponse from(Review review) {
        User user = review.getUser();
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
                review.getImageUrls().getImages(),
                review.getCreatedAt().toLocalDate(),
                option,
                user.getName()
        );
    }
}

