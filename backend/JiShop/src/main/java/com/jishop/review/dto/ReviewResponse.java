package com.jishop.review.dto;

import com.jishop.review.domain.Review;
import com.jishop.review.domain.embed.ImageUrls;
import com.jishop.review.domain.tag.Tag;

import java.time.LocalDate;

public record ReviewResponse(
        int rating,
        String option,
        String content,
        Tag tag,
        ImageUrls images,
        LocalDate createAt
) {
    public static ReviewResponse from(Review review, String option) {
        return new ReviewResponse(
                review.getRating(),
                option,
                review.getContent(),
                review.getTag(),
                review.getImageUrls(),
                review.getCreatedAt().toLocalDate()
        );
    }
}
