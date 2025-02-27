package com.jishop.controller.impl;

import com.jishop.controller.NaverController;
import com.jishop.dto.NaverUserInfo;
import com.jishop.service.NaverService;
import com.jishop.service.impl.NaverServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class NaverControllerImpl implements NaverController {

    private final NaverService naverService;

    @GetMapping("/naver")
    public ResponseEntity<NaverUserInfo> authenticateUser(@RequestParam String code, @RequestParam String state) {
        NaverUserInfo userInfo = naverService.authenticateUserWithNaver(code, state);
        return ResponseEntity.ok(userInfo);
    }
}
