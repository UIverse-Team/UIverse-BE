package com.jishop.service.impl;

import com.jishop.dto.SocialUserInfo;
import com.jishop.dto.TokenResponse;
import com.jishop.service.OauthService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Slf4j
@Service
public class KakaoServiceImpl implements OauthService {

    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.client.secret}")
    private String clientSecret;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    private final HttpSession httpSession;
    private final WebClient kakaoApiWebClient; //사용자정보
    private final WebClient kakaoAuthWebClient; //토쿤

    public KakaoServiceImpl(HttpSession httpSession) {
        this.httpSession = httpSession;
        this.kakaoApiWebClient = WebClient.builder().baseUrl("https://kapi.kakao.com").build();
        this.kakaoAuthWebClient = WebClient.builder().baseUrl("https://kauth.kakao.com/oauth").build();
    }

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
        String savedState = (String) httpSession.getAttribute("oauth2State");
        if (savedState == null || !savedState.equals(state)) {
            throw new IllegalStateException("Invalid state parameter");
        }
        TokenResponse tokenResponse = getKakaoAccessToken(code, state);

        return getKakaoUserInfo(tokenResponse.accessToken());
    }

    private TokenResponse getKakaoAccessToken(String code,String state){
        try{
            return kakaoAuthWebClient.post()
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
        } catch (Exception e){
            log.error("카카오 Access Token을 가져오는 데 오류가 발생했습니다");
            e.printStackTrace();
            throw new RuntimeException("카카오 Access Token을 가져오는 데 오류가 발생했습니다");
        }
    }

    private SocialUserInfo getKakaoUserInfo(String accessToken){
        return kakaoApiWebClient.get()
                .uri("/v2/user/me")
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(SocialUserInfo.class)
                .block();
    }
}
