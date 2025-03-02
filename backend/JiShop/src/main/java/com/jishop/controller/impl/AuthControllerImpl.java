package com.jishop.controller.impl;

import com.jishop.controller.AuthController;
import com.jishop.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthControllerImpl implements AuthController {

    private final UserRepository userRepository;

    @PostMapping("/signIn")
    public void signIn(){

    }

}
