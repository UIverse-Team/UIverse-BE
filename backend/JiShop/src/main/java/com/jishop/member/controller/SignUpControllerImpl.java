package com.jishop.member.controller;

import com.jishop.member.dto.FinalStepRequest;
import com.jishop.member.dto.SignUpFormRequest;
import com.jishop.member.dto.Step1Request;
import com.jishop.member.dto.Step2Request;
import com.jishop.member.dto.request.FindUserRequest;
import com.jishop.member.dto.response.FindUserResponse;
import com.jishop.member.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/signup")
public class SignUpControllerImpl implements SignUpController {

    private final HttpSession session;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/step1")
    public ResponseEntity<String> step1(@RequestBody @Validated Step1Request request){
        // 이메일 저장
        // 이메일 중복 체크
        userService.emailcheck(request);
        SignUpFormRequest form = SignUpFormRequest.of(request.email());
        // 세션에 정보 저장
        session.setAttribute("signUpdate", form);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("step1 완료");
    }

    @PostMapping("/step2")
    public ResponseEntity<String> step2(@RequestBody @Validated Step2Request request){
        SignUpFormRequest form = (SignUpFormRequest) session.getAttribute("signUpdate");

        if(form == null) {
            return ResponseEntity.badRequest().body("이전 단계를 완료해주세요!");
        }

        String password = passwordEncoder.encode(request.password());
        form = form.withPassword(password);

        session.setAttribute("signUpdate", form);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("step2 완료");
    }

    @PostMapping("/last")
    public ResponseEntity<String> lastStep(@RequestBody @Validated FinalStepRequest request){
        SignUpFormRequest form = (SignUpFormRequest) session.getAttribute("signUpdate");

        if(form.password() == null) {
            return ResponseEntity.badRequest().body("이전 단계를 완료해주세요!");
        }

        form = form.withInformation(request.name(), request.yynumber(), request.gender(), request.phone());

        userService.signUp(form);
        session.removeAttribute("signUpdate");

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("회원가입 완료! 환영합니다!");
    }


}
