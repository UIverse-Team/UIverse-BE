package com.log.dto;

import java.time.LocalDateTime;

public record ReviewRequest(
        Long userId,
        Long productId,
        String productName,
        Long reviewId,
        LocalDateTime viewTime
) {
}
