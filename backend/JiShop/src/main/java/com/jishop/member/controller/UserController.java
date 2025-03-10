package com.jishop.member.controller;

import com.jishop.member.dto.Step1Request;
import com.jishop.member.dto.Step2Request;
import org.springframework.http.ResponseEntity;

public interface UserController {

    ResponseEntity<String> step1(Step1Request request);
    ResponseEntity<String> step2(Step2Request request);
}
