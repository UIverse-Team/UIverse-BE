package com.jishop.dto;

import com.jishop.domain.Notice;

import java.time.LocalDateTime;

public record NoticeDetailResponse(

        String title,
        String content,
        String priority,
        LocalDateTime createdDate
) {
    public static NoticeDetailResponse from(Notice notice) {
        return new NoticeDetailResponse(
                notice.getTitle(),
                notice.getContent(),
                notice.getPriority().name(),
                notice.getCreatedAt()
        );
    }
}
