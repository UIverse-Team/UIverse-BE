package com.jishop.member.controller;

import com.jishop.member.dto.request.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "로컬 회원가입 API")
public interface SignUpController {

    ResponseEntity<String> signUp(SignUpFormRequest request);
}
