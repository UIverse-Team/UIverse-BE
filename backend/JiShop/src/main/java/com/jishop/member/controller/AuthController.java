package com.jishop.member.controller;


import com.jishop.member.domain.User;
import com.jishop.member.dto.request.SignInFormRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.ExecutionException;

@Tag(name = "로컬 로그인 API")
public interface AuthController {
    @Operation(summary = "로컬 회원 로그인")
    ResponseEntity<?> signIn(SignInFormRequest request, HttpServletRequest httpRequest,
                                  HttpServletResponse response) throws ExecutionException, InterruptedException;
    @Operation(summary = "회원 로그아웃")
    ResponseEntity<Void> logout(HttpServletRequest request);
    @Operation(summary = "로그인 상태 체크")
    ResponseEntity<String> checkLogin(User user);
}
