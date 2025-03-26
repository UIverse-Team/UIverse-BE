package com.jishop.log.dto;

import java.time.LocalDateTime;

public record SearchLogDto(
        Long id,
        Long userId,
        String searchKeyword,
        LocalDateTime searchTime
) {
}
