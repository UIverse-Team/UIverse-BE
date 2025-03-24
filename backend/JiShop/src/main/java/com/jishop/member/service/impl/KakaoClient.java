package com.jishop.member.service.impl;

import com.jishop.config.OAuthConfig;
import com.jishop.member.dto.response.KakaoUserResponse;
import com.jishop.member.dto.response.TokenResponse;
import com.jishop.member.service.OAuthClient;
import com.jishop.member.service.OAuthProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
public class KakaoClient implements OAuthClient {

    private final RestClient restClient;
    private final OAuthConfig.OAuthDetails config;

    public String getAuthorizationUrl(String state) {
        return config.getAuthorizationUri() +
                "?client_id=" + config.getClientId() +
                "&redirect_uri=" + config.getRedirectUri() +
                "&response_type=code" +
                "&scope=profile_nickname" +
                "&state=" + state;
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
        KakaoUserResponse userInfo = restClient.get()
                .uri(config.getUserInfoUri())
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(KakaoUserResponse.class);

        assert userInfo != null;

        return new KakaoProfile(
                userInfo.id(),
                userInfo.kakaoAccount().email(), // 이 값은 null일 수 있습니다
                userInfo.kakaoAccount().profile().nickname()
        );
    }
}
