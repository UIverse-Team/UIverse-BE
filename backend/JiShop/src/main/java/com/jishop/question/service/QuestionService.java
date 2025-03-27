package com.jishop.question.service;

import com.jishop.member.domain.User;
import com.jishop.question.dto.QuestionRequest;
import com.jishop.question.dto.QuestionResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface QuestionService {

    Long createQuestion(QuestionRequest request, User user);
    ResponseEntity<QuestionResponse> getQuestion(User user, Long questionId);
    ResponseEntity<List<QuestionResponse>> getMyQuestion(User user);
}
