package com.jishop.member.controller;

import com.jishop.member.dto.EmailRequest;
import com.jishop.member.dto.CertifyCodeRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface EmailCertificationController {

    ResponseEntity<String> sendCertification(EmailRequest request, HttpServletResponse response);
    ResponseEntity<String> certifyCode(String token, CertifyCodeRequest request);
}
