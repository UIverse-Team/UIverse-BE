package com.jishop.controller;

import com.jishop.common.util.OauthUriGenerator;
import com.jishop.dto.NaverResourceResponse;
import com.jishop.service.SocialService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {

    private final OauthUriGenerator oauthUriGenerator;
    private final SocialService socialService;

    // 네이버 로그인 url 생성 후 반환
    @GetMapping("/url")
    public String getRedirectUri(){

        return oauthUriGenerator.generateRedURL();
    }

    // 네이버 로그인 콜백으로 전달된 code, state 받기
    @GetMapping("/naver")
    public String naverLogin(@RequestParam(required = false) String code,
                           @RequestParam(required = false) String state){

        NaverResourceResponse resourceResponse = socialService.getTokenAndResource(code, state);
        return resourceResponse.toString();
    }

}
