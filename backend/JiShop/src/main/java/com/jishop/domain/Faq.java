package com.jishop.domain;

import com.jishop.common.util.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "faqs")
public class Faq extends BaseEntity {

    // FAQ 제목
    @Column(nullable = false)
    private String title;

    // FAQ 본문
    @Column(nullable = false)
    private String content;

    // FAQ 유형
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FaqCategory category;
}