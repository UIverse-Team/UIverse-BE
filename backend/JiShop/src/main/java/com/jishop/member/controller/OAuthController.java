package com.jishop.member.controller;

import com.jishop.member.dto.request.OAuthCallbackRequest;
import com.jishop.member.service.OAuthProfile;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Tag(name = "소셜 로그인 API")
public interface OAuthController {

    ResponseEntity<OAuthProfile> login(OAuthCallbackRequest request, HttpSession session);
}
