package com.jishop.member.controller;

import com.jishop.member.domain.LoginType;
import com.jishop.member.domain.User;
import com.jishop.member.dto.SocialUserInfo;
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
public class NaverControllerImpl implements OAuthController {

    private final OauthService naverService;
    private final UserService userService;
    private final HttpSession httpSession;

    public NaverControllerImpl(@Qualifier("naverServiceImpl")OauthService naverService,
                               UserService userService,
                               HttpSession httpSession
    ) {
        this.naverService = naverService;
        this.userService = userService;
        this.httpSession = httpSession;
    }

    @GetMapping("/naver/login")
    public ResponseEntity<String> getNaverAuthUrl(){
        String authUrl = naverService.generateStateAndGetAuthUrl();

        return ResponseEntity.ok(authUrl);
    }

    @Override
    @GetMapping("/naver")
    public ResponseEntity<User> authenticateUser(@RequestParam String code,
                                                 @RequestParam String state) {
        SocialUserInfo userInfo = naverService.authenticateUser(code, state);
        User user = userService.processOAuthUser(userInfo, LoginType.NAVER);
        httpSession.setAttribute("user", user);

        return ResponseEntity.ok(user);
    }
}
