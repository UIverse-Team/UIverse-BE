package com.jishop.controller;

import com.jishop.dto.EmailCertifyCodeRequest;
import com.jishop.dto.EmailRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface EmailCertificationController {

    ResponseEntity<String> sendCertification(EmailRequest request, HttpServletResponse response);
    ResponseEntity<String> certifyCode(String token, EmailCertifyCodeRequest request);
}
