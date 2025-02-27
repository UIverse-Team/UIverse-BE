package com.jishop.service.impl;

import com.jishop.dto.NaverUserResponse;
import com.jishop.dto.SocialUserInfo;
import com.jishop.dto.TokenResponse;
import com.jishop.service.OauthService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Slf4j
@Service
public class NaverServiceImpl implements OauthService {

    @Value("${naver.client.id}")
    private String clientId;

    @Value("${naver.client.secret}")
    private String clientSecret;

    @Value("${naver.redirect.uri}")
    private String redirectUri;

    private HttpSession httpSession;

    private final WebClient naverAuthWebClient;
    private final WebClient naverApiWebClient;

    public NaverServiceImpl(HttpSession httpSession) {
        this.httpSession = httpSession;
        this.naverAuthWebClient = WebClient.builder().baseUrl("https://nid.naver.com").build();
        this.naverApiWebClient = WebClient.builder().baseUrl("https://openapi.naver.com").build();
    }

    @Override
    public String generateStateAndGetAuthUrl() {
        String state = UUID.randomUUID().toString();
        httpSession.setAttribute("oauth2State", state);

        return "https://kauth.kakao.com/oauth/authorize" +
                "?client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code" +
                "&state=" + state;
    }

    public SocialUserInfo authenticateUser(String code, String state) {
        TokenResponse tokenResponse = getNaverAccessToken(code, state);

        return getNaverUserInfo(tokenResponse.accessToken());
    }

    private TokenResponse getNaverAccessToken(String code, String state) {
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
                    .bodyToMono(TokenResponse.class)
                    .block();
        } catch (Exception e) {
            log.error("Error getting Naver access token: {}", e.getMessage(), e);
            throw new RuntimeException("네이버 Access Token 가져오는 데 오류가 발생했습니다");
        }
    }

    private SocialUserInfo getNaverUserInfo(String accessToken) {
        NaverUserResponse userResponse = naverApiWebClient.get()
                .uri("/v1/nid/me")
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(NaverUserResponse.class)
                .block();

        return userResponse.response();
    }


}
