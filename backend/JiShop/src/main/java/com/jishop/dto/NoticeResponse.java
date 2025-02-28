package com.jishop.dto;

import com.jishop.domain.Notice;

import java.time.LocalDateTime;

public record NoticeResponse(
        String title,
        String priority,
        LocalDateTime createdDate

) {
    public static NoticeResponse from(Notice notice) {
        return new NoticeResponse(
                notice.getTitle(),
                notice.getPriority().name(),
                notice.getCreatedAt()
        );
    }
}
