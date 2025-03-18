package com.jishop.member.controller.impl;

import com.jishop.member.controller.EmailCertificationController;
import com.jishop.member.dto.request.EmailRequest;
import com.jishop.member.dto.request.CertifyCodeRequest;
import com.jishop.member.service.EmailCertificationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/emailCertification")
public class EmailCertificationControllerImpl implements EmailCertificationController {

    private final EmailCertificationService emailService;

    @PostMapping("/signup/send")
    public ResponseEntity<String> sendCertificationForSignup(@RequestBody EmailRequest request, HttpServletResponse response) {
        String token = emailService.sendCertificationCodeForSignup(request);
        addCertificationCookie(response, token);

        return ResponseEntity.ok(token);
    }

    @PostMapping("/passwordReset/send")
    public ResponseEntity<String> sendCertificationForPasswordReset(@RequestBody EmailRequest request, HttpServletResponse response) {
        String token = emailService.sendCertificationCodeForPasswordReset(request);
        addCertificationCookie(response, token);

        return ResponseEntity.ok(token);
    }

    @PostMapping("/verify")
    public ResponseEntity<String> certifyCode(@CookieValue(value = "certificationToken") String token,
                                              @RequestBody CertifyCodeRequest request) {
        if(token == null) {
            return ResponseEntity.badRequest().body("인증 토큰이 유효하지 않습니다");
        }

        boolean valid = emailService.certifyCode(token, request.code());

        if(!valid) {
            return ResponseEntity.badRequest().body("인증 실패");
        }

        return ResponseEntity.ok("인증 성공");
    }

    private void addCertificationCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("certificationToken", token);
        cookie.setPath("/");
        cookie.setMaxAge(300);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }
}
