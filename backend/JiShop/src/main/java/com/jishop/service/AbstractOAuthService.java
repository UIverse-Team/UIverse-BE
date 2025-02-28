package com.jishop.service;

import com.jishop.dto.SocialUserInfo;
import com.jishop.dto.TokenResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.UUID;

@Slf4j
public abstract class AbstractOAuthService implements OauthService {

    protected final HttpSession httpSession;
    protected final WebClient authWebClient;
    protected final WebClient apiWebClient;
    protected final String clientId;
    protected final String clientSecret;
    protected final String redirectUri;

    public AbstractOAuthService(HttpSession httpSession,
                                String authBaseUrl,
                                String apiBaseUrl,
                                String clientId,
                                String clientSecret,
                                String redirectUri) {
        this.httpSession = httpSession;
        this.authWebClient = WebClient.builder().baseUrl(authBaseUrl).build();
        this.apiWebClient = WebClient.builder().baseUrl(apiBaseUrl).build();
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
    }

    @Override
    public String generateStateAndGetAuthUrl() {
        String state = UUID.randomUUID().toString();
        httpSession.setAttribute("oauth2State", state);

        return buildAuthUrl(state);
    }

    // 각 서비스에 맞는 인증 URL 생성 로직을 구현하도록 추상 메서드로 선언
    protected abstract String buildAuthUrl(String state);

    @Override
    public SocialUserInfo authenticateUser(String code, String state) {
        String savedState = (String) httpSession.getAttribute("oauth2State");

        if (savedState == null || !savedState.equals(state)) {
            throw new IllegalStateException("Invalid state parameter");
        }

        TokenResponse tokenResponse = getAccessToken(code, state);

        return getUserInfo(tokenResponse.accessToken());
    }

    // 각 서비스별 토큰 발급 로직
    protected abstract TokenResponse getAccessToken(String code, String state);

    // 각 서비스별 사용자 정보 조회 로직
    protected abstract SocialUserInfo getUserInfo(String accessToken);
}