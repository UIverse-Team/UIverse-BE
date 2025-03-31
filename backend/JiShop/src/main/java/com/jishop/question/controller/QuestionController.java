package com.jishop.question.controller;

import com.jishop.member.domain.User;
import com.jishop.question.dto.QuestionRequest;
import com.jishop.question.dto.QuestionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "문의사항 API")
public interface QuestionController {

    @Operation(
            summary = "문의사항 작성",
            description = "로그인한 사용자만 새로운 문의사항 작성"
    )
    ResponseEntity<?> createQuestion(QuestionRequest request, User user);

    @Operation(
            summary = "사용자 문의사항 목록 조회",
            description = "로그인한 사용자의 모든 문의사항 조회 "
    )
    ResponseEntity<List<QuestionResponse>> getMyQuestion(User user);

    @Operation(
            summary = "특정 문의사항 조회",
            description = "문의사항ID에 해당하는 문의사항의 상세 정보 조회"
    )
    ResponseEntity<QuestionResponse> getQuestion(User user, Long id);
}
