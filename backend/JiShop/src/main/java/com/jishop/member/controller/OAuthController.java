package com.jishop.member.controller;

import com.jishop.member.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

public interface OAuthController {
    ResponseEntity<User> authenticateUser(@RequestParam String code,
                                          @RequestParam String state);
}
