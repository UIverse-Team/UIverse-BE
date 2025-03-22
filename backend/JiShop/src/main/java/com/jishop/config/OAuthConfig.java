package com.jishop.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class OAuthConfig {

    @Value("${oauth.client.provider.google.authorization-uri}")
    private String googleAuthorizationUri;

    @Value("${oauth.client.provider.google.token-uri}")
    private String googleTokenUri;

    @Value("${oauth.client.provider.google.user-info-uri}")
    private String googleUserInfoUri;

    @Value("${oauth.registration.google.client-id}")
    private String googleClientId;

    @Value("${oauth.registration.google.client-secret}")
    private String googleClientSecret;

    @Value("${oauth.registration.google.redirect-uri}")
    private String googleRedirectUri;


    // Naver 설정 추가
    @Value("${oauth.client.provider.naver.authorization-uri}")
    private String naverAuthorizationUri;

    @Value("${oauth.client.provider.naver.token-uri}")
    private String naverTokenUri;

    @Value("${oauth.client.provider.naver.user-info-uri}")
    private String naverUserInfoUri;

    @Value("${oauth.registration.naver.client-id}")
    private String naverClientId;

    @Value("${oauth.registration.naver.client-secret}")
    private String naverClientSecret;

    @Value("${oauth.registration.naver.redirect-uri}")
    private String naverRedirectUri;

    // Kakao 설정 추가
    @Value("${oauth.client.provider.kakao.authorization-uri}")
    private String kakaoAuthorizationUri;

    @Value("${oauth.client.provider.kakao.token-uri}")
    private String kakaoTokenUri;

    @Value("${oauth.client.provider.kakao.user-info-uri}")
    private String kakaoUserInfoUri;

    @Value("${oauth.registration.kakao.client-id}")
    private String kakaoClientId;

    @Value("${oauth.registration.kakao.client-secret}")
    private String kakaoClientSecret;

    @Value("${oauth.registration.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    public OAuthDetails getGoogleDetails() {
        return new OAuthDetails(
                googleAuthorizationUri,
                googleTokenUri,
                googleUserInfoUri,
                googleClientId,
                googleClientSecret,
                googleRedirectUri);
    }

    // Naver의 OAuthDetails 반환 메서드 추가
    public OAuthDetails getNaverDetails() {
        return new OAuthDetails(
                naverAuthorizationUri,
                naverTokenUri,
                naverUserInfoUri,
                naverClientId,
                naverClientSecret,
                naverRedirectUri);
    }

    // Kakao의 OAuthDetails 반환 메서드 추가
    public OAuthDetails getKakaoDetails() {
        return new OAuthDetails(
                kakaoAuthorizationUri,
                kakaoTokenUri,
                kakaoUserInfoUri,
                kakaoClientId,
                kakaoClientSecret,
                kakaoRedirectUri);
    }

    public static class OAuthDetails {

        private final String authorizationUri;
        private final String tokenUri;
        private final String userInfoUri;
        private final String clientId;
        private final String clientSecret;
        private final String redirectUri;

        public OAuthDetails(String authorizationUri, String tokenUri, String userInfoUri,
                            String clientId, String clientSecret, String redirectUri) {
            this.authorizationUri = authorizationUri;
            this.tokenUri = tokenUri;
            this.userInfoUri = userInfoUri;
            this.clientId = clientId;
            this.clientSecret = clientSecret;
            this.redirectUri = redirectUri;
        }

        public String getAuthorizationUri() {
            return authorizationUri;
        }

        public String getTokenUri() {
            return tokenUri;
        }

        public String getUserInfoUri() {
            return userInfoUri;
        }

        public String getClientId() {
            return clientId;
        }

        public String getClientSecret() {
            return clientSecret;
        }

        public String getRedirectUri() {
            return redirectUri;
        }
    }
}