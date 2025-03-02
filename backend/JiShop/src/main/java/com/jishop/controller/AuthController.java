package com.jishop.controller;


import com.jishop.dto.SignInFormRequest;
import jakarta.servlet.http.HttpSession;

public interface AuthController {

    public void signIn(SignInFormRequest request, HttpSession session);
}
