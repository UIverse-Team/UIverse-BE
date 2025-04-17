package com.jishop.log;

import java.time.LocalDateTime;

public record LogEntryDto(
        Long id,
        Long userId,
        String pageUrl,
        LocalDateTime visitTime,
        LocalDateTime exitTime,
        Integer durationSeconds
) {
}
