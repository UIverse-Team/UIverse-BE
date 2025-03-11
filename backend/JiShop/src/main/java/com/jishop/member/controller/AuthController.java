package com.jishop.member.controller;


import com.jishop.member.dto.SignInFormRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "로컬 로그인 API")
public interface AuthController {

    ResponseEntity<String> signIn(SignInFormRequest request);
}
