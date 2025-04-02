package com.jishop.member.controller;

import com.jishop.member.dto.request.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "로컬 회원가입 API")
public interface SignUpController {

    @Operation(summary = "로컬 회원 가입")
    ResponseEntity<String> signUp(SignUpFormRequest request);
}
