package com.jishop.service;

import com.jishop.dto.SignInFormRequest;
import com.jishop.dto.SignUpFormRequest;
import jakarta.servlet.http.HttpSession;

public interface LoginService {

    void signUp(SignUpFormRequest form);

    void signIn(SignInFormRequest form, HttpSession session);
}
