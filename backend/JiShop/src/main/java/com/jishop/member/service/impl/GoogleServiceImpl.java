package com.jishop.member.service.impl;

import com.jishop.member.dto.response.SocialUserInfo;
import com.jishop.member.dto.response.TokenResponse;
import com.jishop.member.service.AbstractOAuthService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;

@Slf4j
@Service
public class GoogleServiceImpl extends AbstractOAuthService {

    public GoogleServiceImpl(HttpSession httpSession,
                             @Value("${google.client.id}") String clientId,
                             @Value("${google.client.secret}") String clientSecret,
                             @Value("${google.redirect-uri}") String redirectUri) {
        super(httpSession,
                "https://oauth2.googleapis.com",
                "https://www.googleapis.com",
                clientId,
                clientSecret,
                redirectUri
        );
    }

    @Override
    protected String buildAuthUrl(String state) {
        return "https://accounts.google.com/o/oauth2/v2/auth" +
                "?client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code" +
                "&scope=email profile" +
                "&state=" + state;
    }

    @Override
    protected TokenResponse getAccessToken(String code, String state) {
        try {
            return authWebClient.post()
                    .uri("/token")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData("client_id", clientId)
                            .with("client_secret", clientSecret)
                            .with("code", code)
                            .with("state", state)
                            .with("grant_type", "authorization_code")
                            .with("redirect_uri", redirectUri))
                    .retrieve()
                    .bodyToMono(TokenResponse.class)
                    .block();
        } catch (Exception e) {
            log.error("Error getting Google access token: {}", e.getMessage(), e);
            throw new RuntimeException("구글 Access Token 가져오는 데 오류가 발생했습니다");
        }
    }

    @Override
    protected SocialUserInfo getUserInfo(String accessToken) {
        return apiWebClient.get()
                .uri("/oauth2/v2/userinfo")
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(SocialUserInfo.class)
                .block();
    }
}