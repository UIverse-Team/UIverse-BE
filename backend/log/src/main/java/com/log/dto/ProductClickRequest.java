package com.log.dto;

import java.time.LocalDate;

public record ProductClickRequest(
        Long userId,
        Long productId,
        String productName,
        LocalDate clickTime
) {
    public ProductClickRequest addUserId(Long userId) {

        if (userId == null) userId = 900000000L;

        return new ProductClickRequest(userId, productId, productName, clickTime);
    }

    public String makeRedisKey() {
        return String.format("%s:%s:%s", userId, productId, clickTime.toString());
    }

    public String makeClickRedisKey() {
        return String.format("%s:%s:%s:%s", "product","click", productId, clickTime.toString());
    }
}
