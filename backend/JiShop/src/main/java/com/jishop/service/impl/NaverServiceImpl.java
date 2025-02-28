package com.jishop.service.impl;

import com.jishop.dto.SocialUserInfo;
import com.jishop.dto.TokenResponse;
import com.jishop.service.AbstractOAuthService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;

@Slf4j
@Service
public class NaverServiceImpl extends AbstractOAuthService {

    public NaverServiceImpl(HttpSession httpSession,
                             @Value("${naver.client.id}") String clientId,
                             @Value("${naver.client.secret}") String clientSecret,
                             @Value("${naver.redirect.uri}") String redirectUri) {
        super(httpSession,
                "https://nid.naver.com",
                "https://openapi.naver.com",
                clientId,
                clientSecret,
                redirectUri
        );
    }

    @Override
    protected String buildAuthUrl(String state) {
        return "https://nid.naver.com/oauth2.0/authorize" +
                "?client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code" +
                "&state=" + state;
    }

    @Override
    protected TokenResponse getAccessToken(String code, String state) {
        try {
            return authWebClient.post()
                    .uri("/oauth2.0/token")
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
            log.error("Error getting NAVER access token: {}", e.getMessage(), e);
            throw new RuntimeException("네이버 Access Token 가져오는 데 오류가 발생했습니다");
        }
    }

    @Override
    protected SocialUserInfo getUserInfo(String accessToken) {
        return apiWebClient.get()
                .uri("/v1/nid/me")
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(SocialUserInfo.class)
                .block();
    }
}