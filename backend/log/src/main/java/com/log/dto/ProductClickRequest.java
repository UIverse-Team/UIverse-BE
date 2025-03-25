package com.log.dto;

import java.time.LocalDateTime;

public record ProductClickRequest(
        Long userId,
        Long productId,
        String productName,
        LocalDateTime clickTime
) {
}
