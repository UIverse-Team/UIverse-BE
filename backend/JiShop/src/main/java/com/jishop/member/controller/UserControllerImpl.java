package com.jishop.member.controller;

import com.jishop.member.dto.request.FindUserRequest;
import com.jishop.member.dto.response.FindUserResponse;
import com.jishop.member.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserControllerImpl {

    private final UserService userService;

    @PostMapping("/idfind")
    public FindUserResponse findUser(@RequestBody FindUserRequest request){
        return userService.findUser(request);
    }
}
