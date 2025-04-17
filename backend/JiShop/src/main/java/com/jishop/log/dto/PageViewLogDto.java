package com.jishop.log.dto;

import java.time.LocalDateTime;

public record PageViewLogDto(
        Long id,
        Long userId,
        String pageUrl,
        LocalDateTime visitTime,
        LocalDateTime exitTime,
        Integer durationSeconds
) { }
