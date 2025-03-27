package com.jishop.question.dto;

import com.jishop.question.domain.Question;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record QuestionResponse(
        Long categoryId,
        String orderId,
        String content,
        String image1,
        String image2,
        String image3,
        String answerContent,
        String answerStatus,
        LocalDateTime answeredAt

) {
    public static QuestionResponse fromQuestion(Question question) {
        return new QuestionResponse(
                question.getQuestionCategory().getId(),
                question.getOrder() != null ? question.getOrder().getOrderNumber() : null,
                question.getQuestionContent(),
                question.getImage1(),
                question.getImage2(),
                question.getImage3(),
                question.getAnswerContent(),
                question.getAnswerStatus().name(),
                question.getAnsweredAt()
        );
    }
}
