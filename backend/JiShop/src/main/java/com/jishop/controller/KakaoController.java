package com.jishop.controller;

import com.jishop.dto.KakaoUserInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

public interface KakaoController {

    ResponseEntity<KakaoUserInfo> authenticateUser(@RequestParam String code);
}
