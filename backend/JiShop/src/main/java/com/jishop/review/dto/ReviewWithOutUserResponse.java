package com.jishop.review.dto;

import com.jishop.member.domain.User;
import com.jishop.review.domain.Review;
import com.jishop.review.domain.tag.Tag;

import java.time.LocalDate;
import java.util.List;

public record ReviewWithOutUserResponse(
        Tag tag,
        int rating,
        String content,
        int likeCount,
        List<String> images,
        LocalDate createAt,
        String option,
        String name
) {
    public static ReviewWithOutUserResponse from(Review review) {
        User user = review.getUser();
        String[] split = review.getProductSummary().split(";");
        String option = null;
        if (split.length == 3) {
            option = split[1];
        }

        return new ReviewWithOutUserResponse(
                review.getTag(),
                review.getRating(),
                review.getContent(),
                review.getLikeCount(),
                review.getImageUrls().getImages(),
                review.getCreatedAt().toLocalDate(),
                option,
                user.getName()
        );
    }
}
