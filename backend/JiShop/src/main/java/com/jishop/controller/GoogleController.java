package com.jishop.controller;

import com.jishop.dto.GoogleUserInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

public interface GoogleController {

    ResponseEntity<GoogleUserInfo> authenticateUser(@RequestParam String code);
}
