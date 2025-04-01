package com.log.dto;

import java.time.LocalDateTime;

public record ReviewRequest(
        Long userId,
        Long productId,
        String productName,
        Long reviewId,
        LocalDateTime viewTime
) {
    public ReviewRequest addUserId(Long userId) {
        return new ReviewRequest(userId, productId, productName, reviewId, viewTime);
    }
}
