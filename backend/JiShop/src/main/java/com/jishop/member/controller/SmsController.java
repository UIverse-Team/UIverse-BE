package com.jishop.member.controller;

import com.jishop.member.dto.request.SmsRequest;
import com.jishop.member.dto.request.CertifyCodeRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "SMS 인증 API")
public interface SmsController {

    @Operation(summary = "인증코드 발송")
    ResponseEntity<String> sendVerificationCode(SmsRequest request,
                                                HttpServletResponse response);

    @Operation(summary = "인증코드 확인")
    ResponseEntity<String> verifyCode( CertifyCodeRequest request,
                                       String token);
}
