package com.jishop.review.dto;

import com.jishop.member.domain.User;
import com.jishop.order.domain.OrderDetail;
import com.jishop.product.domain.Product;
import com.jishop.review.domain.Review;
import com.jishop.review.domain.tag.Tag;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record ReviewRequest(

        @NotBlank(message = "주문 상세 아이디 필요합니다.")
        Long orderDetailId,

        @NotBlank(message = "리뷰 내용은 필요합니다.")
        String content,

        Tag tag,

        int rating
) {
    public ReviewRequest {
        if (rating < 1 || rating > 5)
            throw new IllegalArgumentException("별점은 1~5점을 해야한다.");
    }

    public Review toEntity(List<String> images, Product product, OrderDetail orderDetail, User user, String productSummary) {
        // todo: order 랑 orderDtail 추후 저장.
        return Review.builder()
                .tag(tag)
                .rating(rating)
                .content(content)
                .user(user)
                .product(product)
                .imageUrls(images)
                .orderDetail(orderDetail)
                .productSummary(productSummary)
                .build();
    }
}
