package com.jishop.controller.impl;

import com.jishop.controller.AuthController;
import com.jishop.dto.SignInFormRequest;
import com.jishop.service.LoginService;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthControllerImpl implements AuthController {

    private final LoginService service;

    @PostMapping("/signIn")
    public void signIn(@RequestBody SignInFormRequest request, HttpSession session) {
        service.signIn(request, session);
    }

}
