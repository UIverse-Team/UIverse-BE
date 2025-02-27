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
public class NaverControllerImpl implements OauthController {

    private final OauthService naverService;

    public NaverControllerImpl(@Qualifier("naverServiceImpl")OauthService naverService) {
        this.naverService = naverService;
    }

    @GetMapping("/naver")
    public ResponseEntity<SocialUserInfo> authenticateUser(@RequestParam String code, @RequestParam String state) {
        SocialUserInfo userInfo = naverService.authenticateUser(code, state);

        return ResponseEntity.ok(userInfo);
    }
}
