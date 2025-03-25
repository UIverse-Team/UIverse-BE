package com.jishop.member.controller;

import com.jishop.member.service.OAuthProfile;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Tag(name = "소셜 로그인 API")
public interface OAuthController {

    ResponseEntity<String> login(String provider);
    void callback(String provider, String code, HttpServletResponse response) throws IOException;
}
