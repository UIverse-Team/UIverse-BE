package com.jishop.service.impl;

import com.jishop.dto.KakaoTokenResponse;
import com.jishop.dto.KakaoUserInfo;
import com.jishop.service.KakaoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
public class KakaoServiceImpl implements KakaoService {

    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.client.secret}")
    private String clientSecret;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    private final WebClient kakaoAuthWebClient; //토쿤
    private final WebClient kakaoApiWebClient; //사용자정보

    public KakaoServiceImpl(){
        this.kakaoApiWebClient = WebClient.builder().baseUrl("https://kapi.kakao.com").build();
        this.kakaoAuthWebClient = WebClient.builder().baseUrl("https://kauth.kakao.com/oauth").build();
    }

    public KakaoUserInfo authenticateUserWithKakao(String code){
        KakaoTokenResponse tokenResponse = getKakaoAccessToken(code);

        return getKakaoUserInfo(tokenResponse.getAccessToken());
    }

    private KakaoTokenResponse getKakaoAccessToken(String code){
        try{
            return kakaoAuthWebClient.post()
                    .uri("/token")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData("client_id", clientId)
                            .with("client_secret", clientSecret)
                            .with("code", code)
                            .with("grant_type", "authorization_code")
                            .with("redirect_uri", redirectUri))
                    .retrieve()
                    .bodyToMono(KakaoTokenResponse.class)
                    .block();
        } catch (Exception e){
            log.error("카카오 Access Token을 가져오는 데 오류가 발생했습니다");
            e.printStackTrace();

            throw new RuntimeException("카카오 Access Token을 가져오는 데 오류가 발생했습니다");
        }
    }

    private KakaoUserInfo getKakaoUserInfo(String accessToken){
        return kakaoApiWebClient.get()
                .uri("/v2/user/me")
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(KakaoUserInfo.class)
                .block();
    }
}
