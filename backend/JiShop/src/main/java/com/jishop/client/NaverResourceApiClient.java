package com.jishop.client;

import com.jishop.dto.NaverResourceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

// 밑 어노테이션으로 설정 파일에서 지정한 리소스 URI로 요청을 보냄
@FeignClient(name = "naverResource", url = "${oauth2.naver.resource-uri}")
public interface NaverResourceApiClient { //Naver의 사용자 리소스 API를 호출하기 위한 리소스

    // Get 요청시 Authorization 헤더에 토큰 타입 accessToken 형태의 값을 전달해 사용자 정보를 요청
    @GetMapping
    NaverResourceResponse getResource(@RequestHeader("Authorization") String headers);
}
