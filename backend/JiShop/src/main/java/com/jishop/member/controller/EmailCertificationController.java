package com.jishop.member.controller;

import com.jishop.member.dto.request.EmailRequest;
import com.jishop.member.dto.request.CertifyCodeRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

@Tag(name = "이메일 인증 API")
public interface EmailCertificationController {

    ResponseEntity<String> sendCertificationForSignup(EmailRequest request, HttpServletResponse response);
    ResponseEntity<String> sendCertificationForPasswordReset(EmailRequest request, HttpServletResponse response);
    ResponseEntity<String> certifyCode(String token, CertifyCodeRequest request);
}