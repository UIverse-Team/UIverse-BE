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
public class GoogleControllerImpl implements OAuthController {

    private final OauthService googleService;
    private final UserService userService;

    public GoogleControllerImpl(@Qualifier("googleServiceImpl") OauthService googleService, UserService userService) {
        this.googleService = googleService;
        this.userService = userService;
    }
    
    @GetMapping("/google/login")
    public ResponseEntity<String> getGoogleAuthUrl() {
        String authUrl = googleService.generateStateAndGetAuthUrl();

        return ResponseEntity.ok(authUrl);
    }

    @Override
    @GetMapping("/google")
    public ResponseEntity<User> authenticateUser(@RequestParam String code,
                                                           @RequestParam String state) {
        SocialUserInfo userInfo = googleService.authenticateUser(code, state);

        User user = userService.processOAuthUser(userInfo, LoginType.GOOGLE);

        return ResponseEntity.ok(user);
    }
}
