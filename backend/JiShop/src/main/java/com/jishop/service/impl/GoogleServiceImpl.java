package com.jishop.service.impl;

import com.jishop.dto.SocialUserInfo;
import com.jishop.dto.TokenResponse;
import com.jishop.service.OauthService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Slf4j
@Service
public class GoogleServiceImpl implements OauthService {

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.redirect-uri}")
    private String redirectUri;

    private HttpSession httpSession;

    private final WebClient googleAuthWebClient;
    private final WebClient googleApiWebClient;

    public GoogleServiceImpl(HttpSession httpSession) {
        this.httpSession = httpSession;
        this.googleAuthWebClient = WebClient.builder().baseUrl("https://oauth2.googleapis.com").build();
        this.googleApiWebClient = WebClient.builder().baseUrl("https://www.googleapis.com").build();
    }

    @Override
    public String generateStateAndGetAuthUrl() {
        String state = UUID.randomUUID().toString();
        httpSession.setAttribute("oauth2State", state);

        return "https://accounts.google.com/o/oauth2/v2/auth" +
                "?client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code" +
                "&scope=email profile" +
                "&state=" + state;
    }

    public SocialUserInfo authenticateUser(String code,String state) {
        String savedState = (String) httpSession.getAttribute("oauth2State");
        if (savedState == null || !savedState.equals(state)) {
            throw new IllegalStateException("Invalid state parameter");
        }
        TokenResponse tokenResponse = getGoogleAccessToken(code, state);

        return getGoogleUserInfo(tokenResponse.accessToken());
    }

    private TokenResponse getGoogleAccessToken(String code, String state) {
        try {
            return googleAuthWebClient.post()
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
            e.printStackTrace();
            throw new RuntimeException("구글 Access Token 가져오는 데 오류가 발생했습니다");
        }
    }

    private SocialUserInfo getGoogleUserInfo(String accessToken) {
        return googleApiWebClient.get()
                .uri("/oauth2/v2/userinfo")
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(SocialUserInfo.class)
                .block();
    }
}
