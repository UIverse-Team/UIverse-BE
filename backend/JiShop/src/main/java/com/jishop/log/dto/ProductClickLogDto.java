package com.jishop.log.dto;

import java.time.LocalDate;

public record ProductClickLogDto(
        Long id,
        Long userId,
        Long productId,
        String productName,
        LocalDate clickTime
) {
}
