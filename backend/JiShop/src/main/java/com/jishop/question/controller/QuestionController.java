package com.jishop.question.controller;

import com.jishop.member.domain.User;
import com.jishop.question.dto.QuestionRequest;
import com.jishop.question.dto.QuestionResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "문의사항 API")
public interface QuestionController {

    ResponseEntity<?> createQuestion(QuestionRequest request, User user);
    ResponseEntity<?> getMyQuestion(User user);
    ResponseEntity<QuestionResponse> getQuestion(User user, Long id);
}
