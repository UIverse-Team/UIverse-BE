package com.jishop.member.controller;

import com.jishop.member.domain.User;
import com.jishop.member.dto.SignInFormRequest;
import com.jishop.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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
    private final HttpSession session;

    @PostMapping("/signin")
    public ResponseEntity<String> signIn(@RequestBody SignInFormRequest request) {
        service.signIn(request, session);

        User user = (User) session.getAttribute("user");
        String welcomeMessage = service.generateWelcomeMessage(user);
        // 회원명으로 환영합니다! 메세지로 바꾸기
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(welcomeMessage);
    }
}