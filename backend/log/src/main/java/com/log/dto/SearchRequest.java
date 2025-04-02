package com.log.dto;

import java.time.LocalDateTime;

public record SearchRequest(
        Long userId,
        String searchKeyword,
        LocalDateTime searchTime
) {
    public SearchRequest addUserId(Long userid) {
        return new SearchRequest(userid, searchKeyword, searchTime);
    }
}
