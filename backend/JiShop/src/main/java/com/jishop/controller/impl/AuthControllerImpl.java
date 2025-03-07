package com.jishop.controller.impl;

import com.jishop.controller.AuthController;
import com.jishop.dto.SignInFormRequest;
import com.jishop.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthControllerImpl implements AuthController {

    private final UserService service;

    @PostMapping("/signIn")
    public ResponseEntity<String> signIn(@RequestBody SignInFormRequest request, HttpSession session) {
        service.signIn(request, session);

        return ResponseEntity.ok("로그인 성공!");
    }

}
