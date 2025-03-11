package com.jishop.member.controller;

import com.jishop.member.domain.LoginType;
import com.jishop.member.dto.SocialUserInfo;
import com.jishop.member.service.OauthService;
import com.jishop.member.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class KakaoControllerImpl implements OAuthController {

    private final OauthService kakaoService;
    private final UserService userService;
    private final HttpSession httpSession;

    public KakaoControllerImpl(@Qualifier("kakaoServiceImpl") OauthService kakaoService,
                               UserService userService,
                               HttpSession httpSession
    ) {
        this.kakaoService = kakaoService;
        this.userService = userService;
        this.httpSession = httpSession;
    }

    @GetMapping("/kakao/login")
    public ResponseEntity<String> getKakaoAuthUrl() {
        String authUrl = kakaoService.generateStateAndGetAuthUrl();

        return ResponseEntity.ok(authUrl);
    }

    @Override
    @GetMapping("/kakao")
    public ResponseEntity<String> authenticateUser(@RequestParam String code,
                                                 @RequestParam String state) {
        SocialUserInfo userInfo = kakaoService.authenticateUser(code, state);
        Long userId = userService.processOAuthUser(userInfo, LoginType.KAKAO);
        httpSession.setAttribute("userId", userId);

        return ResponseEntity.ok("환영합니다!");
    }
}
