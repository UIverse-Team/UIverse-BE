package com.jishop.member.controller;

import com.jishop.member.dto.SmsRequest;
import com.jishop.member.dto.CertifyCodeRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "sms 인증 API")
public interface SmsController {

    ResponseEntity<String> sendVerificationCode(@RequestBody SmsRequest request,
                                                HttpServletResponse response);
    ResponseEntity<String> verifyCode(@RequestBody CertifyCodeRequest request,
                                      @CookieValue(name = "verificationToken", required = false) String token);
}
