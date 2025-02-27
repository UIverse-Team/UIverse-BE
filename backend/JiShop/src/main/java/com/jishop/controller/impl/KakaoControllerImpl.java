package com.jishop.controller.impl;

import com.jishop.controller.KakaoController;
import com.jishop.dto.KakaoUserInfo;
import com.jishop.service.impl.KakaoServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.json.JSONObject;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class KakaoControllerImpl implements KakaoController {

    private final KakaoServiceImpl kakaoService;

    @GetMapping("/kakao")
    public ResponseEntity<KakaoUserInfo> authenticateUser(@RequestParam String code){
        KakaoUserInfo userInfo = kakaoService.authenticateUserWithKakao(code);

        return ResponseEntity.ok(userInfo);
    }



    // 카카오 로그인 리디렉션 처리
    @GetMapping("/code")
    public String getCode(@RequestParam String code) {
        return "Code: " + code;
    }

    // 카카오 인증 후 토큰 발급 및 사용자 정보 조회
    @GetMapping("/auth")
    public ResponseEntity<String> auth(@RequestParam String code) {
        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // 파라미터 설정
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(generateParam(code), headers);

        // RestTemplate을 사용하여 토큰 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        System.out.println("Kakao Token Response: " + response.getBody());

        // 응답에서 access_token 추출
        JSONObject jsonResponse = new JSONObject(response.getBody());
        String accessToken = jsonResponse.getString("access_token");

        System.out.println("Access Token: " + accessToken);

        // 사용자 정보 조회
        JSONObject userInfo = findUserInfo(accessToken);

        System.out.println("User Info: " + userInfo);
        // 사용자 정보 반환
        return ResponseEntity.ok(userInfo.toString());
    }

    // 파라미터 구성
    private MultiValueMap<String, String> generateParam(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "6fbfbaabc852675644980a7fcdae981d");
        params.add("redirect_uri", "http://localhost:8080/kakao/code");
        params.add("code", code);
        params.add("client_secret", "NoccA9AynxAMfar8g3TOLciTkuGpVOTK");

        return params;
    }

    // 사용자 정보 조회 메서드
    private JSONObject findUserInfo(String accessToken) {
        // WebClient를 사용하여 카카오 API에서 사용자 정보 조회
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                kakaoUserInfoRequest,
                String.class
        );

        return new JSONObject(response.getBody());
    }
}
