package com.jishop.member.controller;


import com.jishop.member.dto.request.SignInFormRequest;
import com.jishop.member.dto.request.UserNameRequest;
import com.jishop.member.dto.request.UserPhoneRequest;
import com.jishop.member.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

@Tag(name = "로컬 로그인 API")
public interface AuthController {

    ResponseEntity<String> signIn(SignInFormRequest request);
    ResponseEntity<Void> logout(HttpServletRequest request);
}
