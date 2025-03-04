package com.jishop.controller.impl;

import com.jishop.controller.EmailCertificationController;
import com.jishop.dto.EmailCertifyCodeRequest;
import com.jishop.dto.EmailRequest;
import com.jishop.service.EmailCertificationService;
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

    @PostMapping("/send")
    public ResponseEntity<String> sendCertification(@RequestBody EmailRequest request, HttpServletResponse response) {
        String token = emailService.sendCerificationCode(request);
        Cookie cookie = new Cookie("certificationToken", token);
        cookie.setPath("/");
        cookie.setMaxAge(300);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        return ResponseEntity.ok(token);
    }

    @PostMapping("/verify")
    public ResponseEntity<String> certifyCode(@CookieValue(value = "certificationToken") String token,
                                              @RequestBody EmailCertifyCodeRequest request) {
        if(token == null) {
            return ResponseEntity.badRequest().body("인증 토큰이 유효하지 않습니다");
        }
        boolean valid = emailService.certifyCode(token, request.code());
        if(valid) {
            return ResponseEntity.ok("인증 성공");
        }else {
            return ResponseEntity.badRequest().body("인증 실패");
        }
    }
}
