package com.jishop.faq.domain;

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
    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // FAQ 카테고리
        // FAQ는 여러 개의 카테고리를 가질 수 있어야한다.
        // "배송", "인기있는 FAQ" 같은 중복 카테고리를 허용해야 한다.
        // FAQ 테이블 하나로만 관리해야 한다.(카테고리 테이블 별도 설계❌)
//    @Enumerated(EnumType.STRING)
//    @ElementCollection(fetch = FetchType.EAGER)
//    @CollectionTable(
//            name = "faq_categories",
//            joinColumns = @JoinColumn(name = "faq_id")
//    )
//    private Set<FaqCategory> category = new HashSet<>();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FaqCategory category;

    // 인기있는 FAQ
    @Column(nullable = false)
    private boolean isPopular;
}