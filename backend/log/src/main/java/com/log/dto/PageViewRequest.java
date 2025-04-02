package com.log.dto;

import java.time.LocalDateTime;

public record PageViewRequest(

        Long userId,
        String pageUrl,
        LocalDateTime visitTime,
        LocalDateTime exitTime,
        Integer durationSeconds
) {
    public PageViewRequest addUserId(Long userId) {
        return new PageViewRequest(userId, pageUrl, visitTime, exitTime, durationSeconds);
    }
}
