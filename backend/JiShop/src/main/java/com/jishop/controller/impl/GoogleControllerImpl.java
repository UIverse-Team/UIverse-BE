package com.jishop.controller.impl;

import com.jishop.controller.OauthController;
import com.jishop.dto.SocialUserInfo;
import com.jishop.service.OauthService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class GoogleControllerImpl implements OauthController {

    private final OauthService googleService;

    public GoogleControllerImpl(@Qualifier("googleServiceImpl") OauthService googleService) {
        this.googleService = googleService;
    }

    @GetMapping("/google/login")
    public ResponseEntity<String> getKakaoAuthUrl() {
        String authUrl = googleService.generateStateAndGetAuthUrl();
        return ResponseEntity.ok(authUrl);
    }

    @GetMapping("/google")
    public ResponseEntity<SocialUserInfo> authenticateUser(@RequestParam String code,@RequestParam String state) {
        SocialUserInfo userInfo = googleService.authenticateUser(code, state);

        return ResponseEntity.ok(userInfo);
    }
}
