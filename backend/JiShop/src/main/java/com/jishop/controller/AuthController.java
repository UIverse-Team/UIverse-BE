package com.jishop.controller;


import com.jishop.dto.SignInFormRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;

public interface AuthController {

    ResponseEntity<String> signIn(SignInFormRequest request);
}
