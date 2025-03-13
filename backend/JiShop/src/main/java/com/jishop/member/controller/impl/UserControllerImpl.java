package com.jishop.member.controller.impl;

import com.jishop.member.controller.UserController;
import com.jishop.member.dto.request.EmailRequest;
import com.jishop.member.dto.request.FindUserRequest;
import com.jishop.member.dto.request.RecoveryPWRequest;
import com.jishop.member.dto.response.FindUserResponse;
import com.jishop.member.dto.response.UserIdResponse;
import com.jishop.member.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @PostMapping("/recoveryid")
    public FindUserResponse findUser(@RequestBody @Validated FindUserRequest request){
        return userService.findUser(request);
    }

    @PostMapping("/finduser")
    public UserIdResponse emailUser(@RequestBody @Validated EmailRequest request){
        return userService.findUserId(request);
    }

    @PostMapping("/recoverypw")
    public ResponseEntity<String> recoverypw(@RequestBody @Validated RecoveryPWRequest request){
        Long userid = 1l;
        userService.recoveryPW(userid, request);

        return ResponseEntity.ok().body("비밀번호 뱐경완료!");
    }
}
