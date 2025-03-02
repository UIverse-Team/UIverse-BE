package com.jishop.controller.impl;

import com.jishop.controller.OAuthController;
import com.jishop.domain.LoginType;
import com.jishop.domain.User;
import com.jishop.dto.SocialUserInfo;
import com.jishop.service.OauthService;
import com.jishop.service.UserService;
import jakarta.servlet.http.HttpSession;
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
    private final HttpSession httpSession;

    public GoogleControllerImpl(@Qualifier("googleServiceImpl") OauthService googleService,
                                UserService userService,
                                HttpSession httpSession
    ) {
        this.googleService = googleService;
        this.userService = userService;
        this.httpSession = httpSession;
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

        httpSession.setAttribute("user", user);

        return ResponseEntity.ok(user);
    }
}
