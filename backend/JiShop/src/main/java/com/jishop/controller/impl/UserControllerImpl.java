package com.jishop.controller.impl;

import com.jishop.controller.UserController;
import com.jishop.dto.SignUpFormRequest;
import com.jishop.dto.Step1Request;
import com.jishop.dto.Step2Request;
import com.jishop.service.LoginService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/signUp")
public class UserControllerImpl implements UserController {

    private final HttpSession session;
    private final LoginService loginService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/step1")
    public ResponseEntity<String> step1(@RequestBody Step1Request request){
        // 이메일 저장
        SignUpFormRequest form = SignUpFormRequest.of(request.email());
        // 세션에 정보 저장
        session.setAttribute("signUpdate", form);

        return ResponseEntity.ok("step1 완료");
    }

    @PostMapping("/step2")
    public ResponseEntity<String> step2(@RequestBody Step2Request request){
        SignUpFormRequest form = (SignUpFormRequest) session.getAttribute("signUpdate");

        if(form == null) {
            return ResponseEntity.badRequest().body("이전 단계를 완료해주세요!");
        }

        String password = passwordEncoder.encode(request.password());
        form = form.withPassword(password);
        loginService.signUp(form);
        session.removeAttribute("signUpdate");

        return ResponseEntity.ok("가입 완료");
    }

}
