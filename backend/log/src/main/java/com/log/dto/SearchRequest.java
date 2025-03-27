package com.log.dto;

import java.time.LocalDateTime;

public record SearchRequest(
        Long userId,
        String searchKeyword,
        LocalDateTime searchTime
) {
}
