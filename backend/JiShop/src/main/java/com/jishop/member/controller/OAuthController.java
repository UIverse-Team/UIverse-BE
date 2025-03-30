package com.jishop.member.controller;

import com.jishop.member.dto.request.OAuthCallbackRequest;
import com.jishop.member.dto.response.OAuthMetaResponse;
import com.jishop.member.service.OAuthProfile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Tag(name = "소셜 로그인 API")
public interface OAuthController {

    @Operation(summary = "인증 url 관련 값 전달")
    ResponseEntity<OAuthMetaResponse> authorizationUrl(String provider);
    @Operation(summary = "소셜 회원 로그인")
    ResponseEntity<OAuthProfile> login(OAuthCallbackRequest request, HttpSession session);
}
