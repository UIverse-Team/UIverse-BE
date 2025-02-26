package com.jishop.client;

import com.jishop.dto.NaverAccessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

// 설정 파일에서 지정한 토큰 URI로 요청을 보냄
@FeignClient(name = "naverLogin", url ="${oauth2.naver.token-uri}")
public interface NaverApiClient {   // 네이버의 토큰 발급 API와 통신하는 인터페이스

    // Get 요청으로 토큰 발급을 요청, 필요한 파라미터 전달
    @GetMapping
    NaverAccessResponse getAccessToken(
            @RequestParam("grant_type") String grantType,
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("code") String code,
            @RequestParam("state") String state
    );
}
