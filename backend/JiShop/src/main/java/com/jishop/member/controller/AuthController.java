package com.jishop.member.controller;


import com.jishop.member.dto.SignInFormRequest;
import org.springframework.http.ResponseEntity;

public interface AuthController {

    ResponseEntity<String> signIn(SignInFormRequest request);
}
