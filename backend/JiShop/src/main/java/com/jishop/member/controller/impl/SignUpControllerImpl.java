package com.jishop.member.controller.impl;

import com.jishop.member.controller.SignUpController;
import com.jishop.member.dto.request.SignUpFormRequest;
import com.jishop.member.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/signup")
public class SignUpControllerImpl implements SignUpController {

    private final UserService userService;

    @PostMapping()
    public ResponseEntity<String> signUp(@RequestBody @Validated SignUpFormRequest request){
        userService.signUp(request);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("회원가입 완료! 환영합니다!");
    }
}
