package com.jishop.controller;

import com.jishop.dto.SendVerificationRequest;
import com.jishop.dto.VerifyCodeRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;

public interface SmsController {

    ResponseEntity<String> sendVerificationCode(@RequestBody SendVerificationRequest request,
                                                HttpServletResponse response);
    ResponseEntity<String> verifyCode(@RequestBody VerifyCodeRequest request,
                                      @CookieValue(name = "verificationToken", required = false) String token);
}
