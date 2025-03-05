package com.jishop.controller.impl;

import com.jishop.controller.SmsController;
import com.jishop.dto.SendVerificationRequest;
import com.jishop.dto.VerifyCodeRequest;
import com.jishop.service.impl.SmsServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/numberCertification")
public class SmsControllerImpl implements SmsController {

    private final SmsServiceImpl smsService;

    @PostMapping("/send")
    public ResponseEntity<String> sendVerificationCode(@RequestBody SendVerificationRequest request,
                                                       HttpServletResponse response) {
        String token = smsService.sendVerificationCode(request.phoneNumber());

        //토큰을 쿠키에 저장
        Cookie cookie = new Cookie("verificationToken", token);
        cookie.setMaxAge(300); //최대 5분
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok("인증 코드가 전송되었습니다.");
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyCode(@RequestBody VerifyCodeRequest request,
                                             @CookieValue(name = "verificationToken", required = false) String token) {
        if (token == null) {
            return ResponseEntity.badRequest().body("인증 토큰이 존재하지 않습니다");
        }

        //요청 DTO에서 code 추출
        boolean isValid = smsService.verifyCode(token, request.code());

        if (isValid) {
            return ResponseEntity.ok("인증이 완료되었습니다.");
        }
        return ResponseEntity.badRequest().body("잘못된 인증코드입니다.");
    }
}

