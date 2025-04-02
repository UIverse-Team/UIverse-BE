package com.jishop.notice.dto;

import com.jishop.notice.domain.Notice;

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
