package com.jishop.question.domain;

import com.jishop.common.util.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "question_categories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionCategory extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private QuestionCategory parentCategory;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean requireOrderNumber;

    @Builder
    public QuestionCategory(QuestionCategory parentCategory, String name, boolean requireOrderNumber) {
        this.parentCategory = parentCategory;
        this.name = name;
        this.requireOrderNumber = requireOrderNumber;
    }
}

