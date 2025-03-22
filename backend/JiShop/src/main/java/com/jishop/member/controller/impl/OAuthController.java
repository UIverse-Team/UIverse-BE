package com.jishop.member.controller.impl;

import com.jishop.config.OAuthConfig;
import com.jishop.member.dto.response.TokenResponse;
import com.jishop.member.service.OAuthClient;
import com.jishop.member.service.OAuthProfile;
import com.jishop.member.service.UserService;
import com.jishop.member.service.impl.GoogleClient;
import com.jishop.member.service.impl.KakaoClient;
import com.jishop.member.service.impl.NaverClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/oauth")
public class OAuthController {

    private final Map<String, OAuthClient> clients;
    private final UserService userService;

    @Autowired
    public OAuthController(OAuthConfig config, RestClient restClient, UserService userService) {
        this.userService = userService;
        clients = Map.of(
                "google", new GoogleClient(restClient, config.getGoogleDetails()),
                "naver", new NaverClient(restClient, config.getNaverDetails()),
                "kakao", new KakaoClient(restClient, config.getKakaoDetails())
        );
    }

    @GetMapping("/login/{provider}")
    public ResponseEntity<String> login(@PathVariable("provider") String provider) {
        if (!clients.containsKey(provider)) {
            throw new IllegalArgumentException("Unsupported provider: " + provider);
        }
        String state = UUID.randomUUID().toString();

        return ResponseEntity.ok(clients.get(provider).getAuthorizationUrl(state));
    }

    @GetMapping("/callback/{provider}")
    public ResponseEntity<OAuthProfile> callback(@PathVariable("provider") String provider,
                                                 @RequestParam(value = "code", required = false) String code) {
        if (!clients.containsKey(provider)) {
            throw new IllegalArgumentException("Unsupported provider: " + provider);
        }

        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException("Authorization code is missing");
        }

        OAuthClient client = clients.get(provider);
        TokenResponse tokenResponse = client.getAccessToken(code);
        OAuthProfile profile = client.getProfile(tokenResponse.accessToken());
        userService.oauthLogin(profile);

        return ResponseEntity.ok(profile);
    }
}