package com.jishop.domain;

import com.jishop.common.util.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "notices")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
