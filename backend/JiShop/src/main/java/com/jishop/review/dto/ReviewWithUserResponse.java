package com.jishop.review.dto;

import com.jishop.member.domain.User;
import com.jishop.review.domain.Review;
import com.jishop.review.domain.embed.ImageUrls;
import com.jishop.review.domain.tag.Tag;

import java.time.LocalDate;
import java.util.List;

public record ReviewWithUserResponse(
        Tag tag,
        int rating,
        String content,
        int likeCount,
        List<String> images,
        LocalDate createAt,
        String option,
        boolean isLike,
        String name
) {
    public static ReviewWithUserResponse from(Review review, Boolean isLike) {
        User user = review.getUser();

        return new ReviewWithUserResponse(
                review.getTag(),
                review.getRating(),
                review.getContent(),
                review.getLikeCount(),
                review.getImageUrls(),
                review.getCreatedAt().toLocalDate(),
                review.getOptionString(),
                isLike,
                user.getName()
        );
    }
}
