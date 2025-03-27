package com.jishop.question.controller;

import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.member.annotation.CurrentUser;
import com.jishop.member.domain.User;
import com.jishop.question.dto.QuestionRequest;
import com.jishop.question.dto.QuestionResponse;
import com.jishop.question.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/questions")
public class QuestionControllerImpl implements QuestionController {

    private final QuestionService questionService;

    @Override
    @PostMapping
    public ResponseEntity<?> createQuestion(@RequestBody @Valid QuestionRequest request,
                                                   @CurrentUser User user) {
        if(user == null) {
            throw new DomainException(ErrorType.LOGIN_REQUIRED);
        }

        Long questionId = questionService.createQuestion(request, user);

        return ResponseEntity.created(URI.create("/questions/" + questionId)).build();
    }

    @Override
    @GetMapping
    public ResponseEntity<List<QuestionResponse>> getMyQuestion(@CurrentUser User user){
        if(user == null) {
            throw new DomainException(ErrorType.LOGIN_REQUIRED);
        }

        return questionService.getMyQuestion(user);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<QuestionResponse> getQuestion(@CurrentUser User user, @PathVariable Long id) {
        if(user == null) {
            throw new DomainException(ErrorType.LOGIN_REQUIRED);
        }

        return questionService.getQuestion(user, id);
    }
}
