package com.jishop.controller;

import com.jishop.dto.SocialUserInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

public interface OauthController {

    ResponseEntity<SocialUserInfo> authenticateUser(@RequestParam String code, @RequestParam String state);
}
