package com.jishop.member.controller.impl;

import com.jishop.member.annotation.CurrentUser;
import com.jishop.member.controller.AuthController;
import com.jishop.member.domain.User;
import com.jishop.member.dto.request.SignInFormRequest;
import com.jishop.member.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthControllerImpl implements AuthController {

    private final AuthService service;

    @PostMapping("/signin")
    public ResponseEntity<String> signIn(@RequestBody @Valid SignInFormRequest request,
                                         HttpServletRequest httpRequest,
                                         HttpServletResponse httpServletResponse) {
        // todo: IP 주소 로깅을 해야할까??
        // String clientIp = getClientIp(httpRequest);

        HttpSession session = httpRequest.getSession();
        service.signIn(request, session);

        Long userId = (Long) session.getAttribute("userId");
        String welcomeMessage = service.loginStr(userId);

        // CSRF 토큰 생성 및 응답 헤더에 포함
        String csrfToken = UUID.randomUUID().toString();
        session.setAttribute("CSRF_TOKEN", csrfToken);
        httpServletResponse.setHeader("X-CSRF-TOKEN", csrfToken);

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

    @GetMapping()
    public ResponseEntity<String> checkLogin(@CurrentUser User user) {
        Long id = service.checkLogin(user);
        if(id == null) return ResponseEntity.badRequest().body("로그인 타임 종료!");
        return ResponseEntity.ok("로그인 중!");
    }
}