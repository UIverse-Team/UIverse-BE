package com.jishop.review.dto;

import com.jishop.domain.User;
import com.jishop.review.domain.Review;
import com.jishop.review.domain.tag.Tag;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record ReviewRequest(
        @NotBlank(message = "리뷰 내용은 필요합니다.")
        String content,
        @NotBlank(message = "상품 아이디 필요합니다.")
        Long productId,
        @NotBlank(message = "주문 상세 아이디 필요")
        Long orderDetailId,
        int rate,
        Tag tag
) {
    public ReviewRequest {

    }

    public Review toEntity(List<String> images, User user) {
        return Review.builder()
                .tag(tag)
                .rate(rate)
                .content(content)
                .productId(productId)
                .user(user)
                .imageUrls(images)
                .build();
    }
}
