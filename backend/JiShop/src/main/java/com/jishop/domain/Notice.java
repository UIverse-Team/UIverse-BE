package com.jishop.domain;

import com.jishop.common.util.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

@Entity
@Getter
public class Notice extends BaseEntity {

    // 공지사항 제목
    @Column(nullable = false)
    private String title;

    // 공지사항 내용
    @Column(nullable = false)
    private String content;

    // 공지사항 중요도
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NoticePriority priority;
}
