package com.jishop.controller.impl;

import com.jishop.controller.GoogleController;
import com.jishop.dto.GoogleUserInfo;
import com.jishop.service.impl.GoogleServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class GoogleControllerImpl implements GoogleController {

    private final GoogleServiceImpl googleService;

    @GetMapping("/google")
    public ResponseEntity<GoogleUserInfo> authenticateUser(@RequestParam String code) {
        GoogleUserInfo userInfo = googleService.authenticateUserWithGoogle(code);
        return ResponseEntity.ok(userInfo);
    }
}
