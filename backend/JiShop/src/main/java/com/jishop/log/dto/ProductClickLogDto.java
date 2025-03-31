package com.jishop.log.dto;

import java.time.LocalDateTime;

public record ProductClickLogDto(
        Long id,
        Long userId,
        Long productId,
        String productName,
        LocalDateTime clickTime
) {
}
