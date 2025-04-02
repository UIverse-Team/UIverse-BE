package com.jishop.log.dto;

import java.time.LocalDateTime;

public record ReviewLogDto(
        Long id,
        Long userId,
        Long productId,
        String productName,
        Long reviewId,
        LocalDateTime viewTime
) {
}
