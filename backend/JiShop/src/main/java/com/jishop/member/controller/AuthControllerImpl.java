package com.jishop.member.controller;

import com.jishop.member.dto.SignInFormRequest;
import com.jishop.member.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthControllerImpl implements AuthController {

    private final UserService service;
    private final HttpSession session;

    @PostMapping("/signin")
    public ResponseEntity<String> signIn(@RequestBody @Valid SignInFormRequest request) {
        service.signIn(request, session);

        Long userId = (Long) session.getAttribute("userId");
        String welcomeMessage = service.loginStr(userId);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(welcomeMessage);
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            // redis의 세션도 삭제
            session.invalidate();
        }
        return ResponseEntity.ok().build();
    }
}