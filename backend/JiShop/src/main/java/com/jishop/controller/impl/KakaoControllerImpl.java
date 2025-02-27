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
}
