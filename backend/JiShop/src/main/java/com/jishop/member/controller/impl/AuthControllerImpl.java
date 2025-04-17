package com.jishop.member.controller.impl;

import com.jishop.member.annotation.CurrentUser;
import com.jishop.member.controller.AuthController;
import com.jishop.member.domain.User;
import com.jishop.member.dto.request.SignInFormRequest;
import com.jishop.member.service.AuthService;
import com.jishop.queue.service.QueueService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthControllerImpl implements AuthController {

    private final AuthService service;
    private final QueueService queueService;

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody @Valid SignInFormRequest request,
                                         HttpServletRequest httpRequest,
                                         HttpServletResponse httpServletResponse) throws ExecutionException, InterruptedException {
        // todo: IP 주소 로깅을 해야할까?? -> 해결책 다른 나라, 일단 다른 ip인 경우 해당 하면 알림 보내기?
        // String clientIp = getClientIp(httpRequest);

        try {
            // 요청 처리 시작 시 카운터 증가
            queueService.incrementActiveRequests();

            HttpSession session = httpRequest.getSession();
            CompletableFuture<String> future = service.signInType(request, session);
            String taskId = future.get();

            if(taskId.startsWith("immediate-login-")){
                Long userId =(Long) session.getAttribute("userId");
                String welcome = service.loginStr(userId);
                return ResponseEntity.ok(welcome);
            }

            return ResponseEntity.accepted().body(Map.of(
                    "taskId", taskId,
                    "message", "로그인 요청이 대기열에 등록되었습니다."
            ));
        } finally {
            // 요청 처리 완료 시 카운터 감소 (예외 발생해도 실행됨)
            queueService.decrementActiveRequests();
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(@CurrentUser User user, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            // redis의 세션도 삭제
            session.invalidate();
        }
        service.logout(user);
        return ResponseEntity.ok().build();
    }

    @GetMapping()
    public ResponseEntity<String> checkLogin(@CurrentUser User user) {
        Long id = service.checkLogin(user);
        if(id == null) return ResponseEntity.badRequest().body("로그인 타임 종료!");
        return ResponseEntity.ok("로그인 중!");
    }
}