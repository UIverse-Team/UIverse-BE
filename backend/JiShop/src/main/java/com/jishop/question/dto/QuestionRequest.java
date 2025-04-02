package com.jishop.question.dto;

import com.jishop.member.domain.User;
import com.jishop.order.domain.Order;
import com.jishop.question.domain.Question;
import com.jishop.question.domain.QuestionCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record QuestionRequest(
        @NotNull Long categoryId,
        String orderId,
        @NotBlank String content,
        String image1,
        String image2,
        String image3
) {
    public static Question toEntity(QuestionRequest request, QuestionCategory category, User user, Order order) {
        return Question.builder()
                .questionCategory(category)
                .user(user)
                .order(order)
                .questionContent(request.content())
                .image1(request.image1())
                .image2(request.image2())
                .image3(request.image3())
                .build();
    }
}
