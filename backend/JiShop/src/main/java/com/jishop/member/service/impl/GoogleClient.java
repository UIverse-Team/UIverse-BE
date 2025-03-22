package com.jishop.member.service.impl;

import com.jishop.config.OAuthConfig;
import com.jishop.member.dto.response.GoogleUserResponse;
import com.jishop.member.dto.response.TokenResponse;
import com.jishop.member.service.OAuthClient;
import com.jishop.member.service.OAuthProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestClient;

import java.util.Map;

@RequiredArgsConstructor
public class GoogleClient implements OAuthClient {

    private final RestClient restClient;
    private final OAuthConfig.OAuthDetails config;

    @Override
    public String getAuthorizationUrl(String state) {
        return config.getAuthorizationUri() +
                "?client_id=" + config.getClientId() +
                "&redirect_uri=" + config.getRedirectUri() +
                "&response_type=code" +
                "&scope=email profile" +
                "&state=" + state; // state 값 추가
    }

    @Override
    public TokenResponse getAccessToken(String code) {
        return restClient.post()
                .uri(config.getTokenUri())
                .body(Map.of(
                        "code", code,
                        "client_id", config.getClientId(),
                        "client_secret", config.getClientSecret(),
                        "redirect_uri", config.getRedirectUri(),
                        "grant_type", "authorization_code"
                ))
                .retrieve()
                .body(TokenResponse.class);
    }

    @Override
    public OAuthProfile getProfile(String accessToken) {
        GoogleUserResponse userInfo = restClient.get()
                .uri(config.getUserInfoUri())
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(GoogleUserResponse.class);

        assert userInfo != null;

        return new GoogleProfile(userInfo.sub(), userInfo.email(), userInfo.name());
    }
}
