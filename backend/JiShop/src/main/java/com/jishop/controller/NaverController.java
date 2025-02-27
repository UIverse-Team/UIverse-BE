package com.jishop.controller;

import com.jishop.dto.NaverUserInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

public interface NaverController {

    ResponseEntity<NaverUserInfo> authenticateUser(@RequestParam String code, @RequestParam String state);
}
