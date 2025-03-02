package com.jishop.controller.impl;

import com.jishop.controller.OAuthController;
import com.jishop.domain.LoginType;
import com.jishop.domain.User;
import com.jishop.dto.SocialUserInfo;
import com.jishop.service.OauthService;
import com.jishop.service.UserService;
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

    public KakaoControllerImpl(@Qualifier("kakaoServiceImpl") OauthService kakaoService, UserService userService) {
        this.kakaoService = kakaoService;
        this.userService = userService;
    }

    @GetMapping("/kakao/login")
    public ResponseEntity<String> getKakaoAuthUrl() {
        String authUrl = kakaoService.generateStateAndGetAuthUrl();

        return ResponseEntity.ok(authUrl);
    }

    @Override
    @GetMapping("/kakao")
    public ResponseEntity<User> authenticateUser(@RequestParam String code,
                                                           @RequestParam String state) {
        SocialUserInfo userInfo = kakaoService.authenticateUser(code, state);

        User user = userService.processOAuthUser(userInfo, LoginType.KAKAO);

        return ResponseEntity.ok(user);
    }
}
