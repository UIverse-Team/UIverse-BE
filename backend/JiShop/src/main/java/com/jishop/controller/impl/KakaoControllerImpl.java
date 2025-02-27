package com.jishop.controller.impl;

import com.jishop.controller.OauthController;
import com.jishop.dto.SocialUserInfo;
import com.jishop.service.OauthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class KakaoControllerImpl implements OauthController {

    private final OauthService kakaoService;

    public KakaoControllerImpl(@Qualifier("kakaoServiceImpl") OauthService kakaoService) {
        this.kakaoService = kakaoService;
    }

    @GetMapping("/kakao/login")
    public ResponseEntity<String> getKakaoAuthUrl() {
        String authUrl = kakaoService.generateStateAndGetAuthUrl();
        return ResponseEntity.ok(authUrl);
    }

    @GetMapping("/kakao")
    public ResponseEntity<SocialUserInfo> authenticateUser(@RequestParam String code, @RequestParam String state) {
        SocialUserInfo userInfo = kakaoService.authenticateUser(code, state);
        return ResponseEntity.ok(userInfo);
    }
}
