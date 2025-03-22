package com.jishop.member.controller;

import com.jishop.member.service.OAuthProfile;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "소셜 로그인 API")
public interface OAuthController {

    ResponseEntity<String> login(String provider);
    ResponseEntity<OAuthProfile> callback(String provider, String code);
}
