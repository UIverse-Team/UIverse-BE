package com.jishop.member.service.impl;

import com.jishop.member.dto.response.BasicSocialUserInfo;
import com.jishop.member.dto.response.KakaoUserResponse;
import com.jishop.member.dto.response.SocialUserInfo;
import com.jishop.member.dto.response.TokenResponse;
import com.jishop.member.service.AbstractOAuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;

@Service
public class KakaoServiceImpl extends AbstractOAuthService {

    public KakaoServiceImpl(HttpSession httpSession,
                            @Value("${kakao.client.id}") String clientId,
                            @Value("${kakao.client.secret}") String clientSecret,
                            @Value("${kakao.redirect-uri}") String redirectUri) {
        super(httpSession,
                "https://kauth.kakao.com/oauth",
                "https://kapi.kakao.com",
                clientId,
                clientSecret,
                redirectUri
        );
    }

    @Override
    protected String buildAuthUrl(String state) {
        return "https://kauth.kakao.com/oauth/authorize" +
                "?client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code" +
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
            throw new RuntimeException("카카오 Access Token 가져오는 데 오류가 발생했습니다");
        }
    }

    @Override
    protected SocialUserInfo getUserInfo(String accessToken) {
        return apiWebClient.get()
                .uri("/v2/user/me")
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(KakaoUserResponse.class)
                .map(this::convertToSocialUserInfo)
                .block();
    }

    private SocialUserInfo convertToSocialUserInfo(KakaoUserResponse response) {
        return new BasicSocialUserInfo(
                String.valueOf(response.id()),
                response.kakaoAccount().profile().nickname(),
                response.kakaoAccount().email()
        );
    }
}
