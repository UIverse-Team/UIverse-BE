package com.jishop.member.controller;

import com.jishop.member.dto.SmsRequest;
import com.jishop.member.dto.CertifyCodeRequest;
import com.jishop.service.SmsCertificationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/numberCertification")
public class SmsControllerImpl implements SmsController {

    private final SmsCertificationService smsService;

    @PostMapping("/send")
    public ResponseEntity<String> sendVerificationCode(@RequestBody SmsRequest request,
                                                       HttpServletResponse response) {
        String token = smsService.sendVerificationCode(request);

        //토큰을 쿠키에 저장
        Cookie cookie = new Cookie("verificationToken", token);
        cookie.setMaxAge(300); //최대 5분
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok("인증코드가 전송되었습니다");
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyCode(@RequestBody CertifyCodeRequest request,
                                             @CookieValue(name = "verificationToken", required = false) String token) {
        if (token == null)
            return ResponseEntity.badRequest().body("인증 토큰이 존재하지 않습니다");

        if (!smsService.verifyCode(token, request.code()))
            return ResponseEntity.badRequest().body("잘못된 인증코드입니다.");

        return ResponseEntity.ok("인증이 완료되었습니다.");
    }
}

