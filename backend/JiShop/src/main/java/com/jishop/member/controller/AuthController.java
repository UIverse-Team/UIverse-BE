package com.jishop.member.controller;


import com.jishop.member.dto.request.SignInFormRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;

@Tag(name = "로컬 로그인 API")
public interface AuthController {

    ResponseEntity<String> signIn(SignInFormRequest request, HttpServletRequest httpRequest,
                                  HttpServletResponse response);
    ResponseEntity<Void> logout(HttpServletRequest request);
}
