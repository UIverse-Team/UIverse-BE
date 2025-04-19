package com.jishop.review.dto;

import com.jishop.review.domain.Review;

public record ReviewImageResponse(
        Long id,
        String image
) {
    public static ReviewImageResponse from(Review review) {
        return new ReviewImageResponse(
                review.getId(),
                review.getImageUrls().get(0)
        );
    }
}
