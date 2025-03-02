package com.jishop.controller;

import com.jishop.domain.User;
import com.jishop.dto.SocialUserInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

public interface OAuthController {
    ResponseEntity<User> authenticateUser(@RequestParam String code,
                                          @RequestParam String state);
}
