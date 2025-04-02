package com.jishop.question.domain;

import com.jishop.common.util.BaseEntity;
import com.jishop.member.domain.User;
import com.jishop.order.domain.Order;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "questions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BaseEntity {

    @JoinColumn(name = "category_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private QuestionCategory questionCategory;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "order_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @Column(nullable = false)
    private String questionContent;

    // TODO : 문의사항 이미지 처리는 file, review쪽 참고
    private String image1;

    private String image2;

    private String image3;

    private String answerContent;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AnswerStatus answerStatus;

    private LocalDateTime answeredAt;

    @Builder
    public Question(QuestionCategory questionCategory, User user, Order order,
                    String questionContent, String image1, String image2, String image3) {
        this.questionCategory = questionCategory;
        this.user = user;
        this.order = order;
        this.questionContent = questionContent;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.answerStatus = AnswerStatus.WAITING;
    }

    public void updateAnswers(String answerContent, LocalDateTime answeredAt) {
        this.answerContent = answerContent;
        this.answeredAt = answeredAt;
        this.answerStatus = AnswerStatus.ANSWERED;
    }
}
