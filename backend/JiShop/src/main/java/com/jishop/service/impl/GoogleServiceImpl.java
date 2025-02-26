package com.jishop.service.impl;

import com.jishop.dto.GoogleTokenResponse;
import com.jishop.dto.GoogleUserInfo;
import com.jishop.service.GoogleService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GoogleServiceImpl implements GoogleService {

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.redirect-uri}")
    private String redirectUri;

    private final WebClient googleAuthWebClient;
    private final WebClient googleApiWebClient;

    public GoogleServiceImpl() {
        this.googleAuthWebClient = WebClient.builder().baseUrl("https://oauth2.googleapis.com").build();
        this.googleApiWebClient = WebClient.builder().baseUrl("https://www.googleapis.com").build();
    }

    public GoogleUserInfo authenticateUserWithGoogle(String code) {
        GoogleTokenResponse tokenResponse = getGoogleAccessToken(code);
        return getGoogleUserInfo(tokenResponse.getAccessToken());
    }

    private GoogleTokenResponse getGoogleAccessToken(String code) {
        try {
            return googleAuthWebClient.post()
                    .uri("/token")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData("client_id", clientId)
                            .with("client_secret", clientSecret)
                            .with("code", code)
                            .with("grant_type", "authorization_code")
                            .with("redirect_uri", redirectUri))
                    .retrieve()
                    .bodyToMono(GoogleTokenResponse.class)
                    .block();
        } catch (Exception e) {
            System.err.println("Error getting Google access token: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private GoogleUserInfo getGoogleUserInfo(String accessToken) {
        return googleApiWebClient.get()
                .uri("/oauth2/v2/userinfo")
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(GoogleUserInfo.class)
                .block();
    }
}
