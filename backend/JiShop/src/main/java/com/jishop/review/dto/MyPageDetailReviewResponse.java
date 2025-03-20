package com.jishop.review.dto;

import com.jishop.review.domain.Review;
import com.jishop.review.domain.tag.Tag;

public record MyPageDetailReviewResponse(
        Tag tag,
        int rating,
        String content
) {
    public static MyPageDetailReviewResponse from(Review review) {
        return new MyPageDetailReviewResponse(
                review.getTag(),
                review.getRating(),
                review.getContent()
        );
    }
}


