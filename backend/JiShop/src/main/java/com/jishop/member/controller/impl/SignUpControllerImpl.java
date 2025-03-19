package com.jishop.member.controller.impl;

import com.jishop.member.controller.SignUpController;
import com.jishop.member.dto.request.*;
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

    @PostMapping("/step0")
    public ResponseEntity<String> step0(@RequestBody @Validated Step0Request request) {
        SignUpFormRequest form = SignUpFormRequest.of(request.ageAgreement(),
                request.useAgreement(), request.picAgreement(), request.adAgreement());
        session.setAttribute("signUpdate", form);

        return ResponseEntity.ok("step0 완료");
    }

    @PostMapping("/step1")
    public ResponseEntity<String> step1(@RequestBody @Validated Step1Request request){
        userService.emailcheck(request);
        SignUpFormRequest form = (SignUpFormRequest) session.getAttribute("signUpdate");

        if(form == null){
            return ResponseEntity.badRequest().body("이전 단계를 완료해주세요!");
        }
        form = form.withEmail(request.email());
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
