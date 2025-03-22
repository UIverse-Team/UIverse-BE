package com.jishop.member.service.impl;

import com.jishop.config.OAuthConfig;
import com.jishop.member.dto.response.NaverUserResponse;
import com.jishop.member.dto.response.TokenResponse;
import com.jishop.member.service.OAuthClient;
import com.jishop.member.service.OAuthProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
public class NaverClient implements OAuthClient {

    private final RestClient restClient;
    private final OAuthConfig.OAuthDetails config;

    @Override
    public String getAuthorizationUrl(String state) {
        return config.getAuthorizationUri() +
                "?client_id=" + config.getClientId() +
                "&redirect_uri=" + config.getRedirectUri() +
                "&response_type=code" +
                "&state=" + state; // state 값 추가
    }

    @Override
    public TokenResponse getAccessToken(String code) {
        return restClient.post()
                .uri(config.getTokenUri())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(
                        "grant_type=authorization_code" +
                                "&client_id=" + config.getClientId() +
                                "&client_secret=" + config.getClientSecret() +
                                "&redirect_uri=" + config.getRedirectUri() +
                                "&code=" + code
                )
                .retrieve()
                .body(TokenResponse.class);
    }

    @Override
    public OAuthProfile getProfile(String accessToken) {
        NaverUserResponse userInfo = restClient.get()
                .uri(config.getUserInfoUri())
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(NaverUserResponse.class);

        assert userInfo != null;

        return new NaverProfile(
                userInfo.response().id(),
                userInfo.response().email(),
                userInfo.response().name(),
                userInfo.response().gender(),
                userInfo.response().mobile(),
                userInfo.response().birthyear(),
                userInfo.response().birthday()
        );
    }
}
