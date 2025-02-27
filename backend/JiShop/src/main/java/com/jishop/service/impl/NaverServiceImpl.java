package com.jishop.service.impl;

import com.jishop.dto.NaverTokenResponse;
import com.jishop.dto.NaverUserInfo;
import com.jishop.dto.NaverUserResponse;
import com.jishop.service.NaverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
public class NaverServiceImpl implements NaverService {

    @Value("${naver.client.id}")
    private String clientId;

    @Value("${naver.client.secret}")
    private String clientSecret;

    private final WebClient naverAuthWebClient;
    private final WebClient naverApiWebClient;

    public NaverServiceImpl() {
        this.naverAuthWebClient = WebClient.builder().baseUrl("https://nid.naver.com").build();
        this.naverApiWebClient = WebClient.builder().baseUrl("https://openapi.naver.com").build();
    }

    public NaverUserInfo authenticateUserWithNaver(String code, String state) {
        NaverTokenResponse tokenResponse = getNaverAccessToken(code, state);

        return getNaverUserInfo(tokenResponse.getAccessToken());
    }

    private NaverTokenResponse getNaverAccessToken(String code, String state) {
        try {
            return naverAuthWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/oauth2.0/token")
                            .queryParam("grant_type", "authorization_code")
                            .queryParam("client_id", clientId)
                            .queryParam("client_secret", clientSecret)
                            .queryParam("code", code)
                            .queryParam("state", state)
                            .build())
                    .retrieve()
                    .bodyToMono(NaverTokenResponse.class)
                    .block();
        } catch (Exception e) {
            log.error("Error getting Naver access token: {}", e.getMessage(), e);
            throw new RuntimeException("네이버 Access Token 가져오는 데 오류가 발생했습니다");
        }
    }

    private NaverUserInfo getNaverUserInfo(String accessToken) {
        NaverUserResponse userResponse = naverApiWebClient.get()
                .uri("/v1/nid/me")
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(NaverUserResponse.class)
                .block();

        return userResponse.getResponse();
    }
}
