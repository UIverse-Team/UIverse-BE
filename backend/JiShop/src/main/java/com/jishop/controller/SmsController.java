package com.jishop.controller;

import com.jishop.dto.SmsRequest;
import com.jishop.dto.CertifyCodeRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;

public interface SmsController {

    ResponseEntity<String> sendVerificationCode(@RequestBody SmsRequest request,
                                                HttpServletResponse response);
    ResponseEntity<String> verifyCode(@RequestBody CertifyCodeRequest request,
                                      @CookieValue(name = "verificationToken", required = false) String token);
}
