package com.jishop.member.controller;

import com.jishop.member.domain.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "소셜 로그인 API")
public interface OAuthController {
    ResponseEntity<User> authenticateUser(@RequestParam String code,
                                          @RequestParam String state);
}
