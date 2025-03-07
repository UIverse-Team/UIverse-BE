package com.jishop.controller;

import com.jishop.dto.EmailRequest;
import com.jishop.dto.CertifyCodeRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface EmailCertificationController {

    ResponseEntity<String> sendCertification(EmailRequest request, HttpServletResponse response);
    ResponseEntity<String> certifyCode(String token, CertifyCodeRequest request);
}
