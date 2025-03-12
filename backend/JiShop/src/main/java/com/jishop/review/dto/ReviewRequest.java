package com.jishop.review.dto;

import com.jishop.member.domain.User;
import com.jishop.review.domain.Review;
import com.jishop.review.domain.tag.Tag;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record ReviewRequest(
        @NotBlank(message = "리뷰 내용은 필요합니다.")
        String content,
        @NotBlank(message = "상품 아이디 필요합니다.")
        Long orderId,
        @NotBlank(message = "주문 상세 아이디 필요")
        Long orderDetailId,
        Tag tag,
        int rating
) {
    public ReviewRequest {
        if (rating < 1 || rating > 5)
            throw new IllegalArgumentException("별점은 1~5점을 해야한다.");
    }

    public Review toEntity(List<String> images, User user, String productSummary) {
        // todo: order 랑 orderDtail 추후 저장.
        return Review.builder()
                .tag(tag)
                .rating(rating)
                .content(content)
                .productSummary(productSummary)
                .user(user)
                .imageUrls(images)
                .build();
    }
}
