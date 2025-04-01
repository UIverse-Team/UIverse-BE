package com.log.dto;

import java.time.LocalDateTime;

public record ProductClickRequest(
        Long userId,
        Long productId,
        String productName,
        LocalDateTime clickTime
) {
    public ProductClickRequest addUserId(Long userId) {
       return new ProductClickRequest(userId, productId, productName, clickTime);
    }
}
