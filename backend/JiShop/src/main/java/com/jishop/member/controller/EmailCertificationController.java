package com.jishop.member.controller;

import com.jishop.member.dto.request.EmailRequest;
import com.jishop.member.dto.request.CertifyCodeRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

@Tag(name = "이메일 인증 API")
public interface EmailCertificationController {

    @Operation(summary = "회원가입 이메일 인증")
    ResponseEntity<String> sendCertificationForSignup(EmailRequest request, HttpServletResponse response);
    @Operation(summary = "비번 찾기 이메일 인증")
    ResponseEntity<String> sendCertificationForPasswordReset(EmailRequest request, HttpServletResponse response);
    @Operation(summary = "이메일 인증 여부 체크")
    ResponseEntity<String> certifyCode(String token, CertifyCodeRequest request);
}