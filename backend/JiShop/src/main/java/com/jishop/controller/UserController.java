package com.jishop.controller;

import com.jishop.dto.Step1Request;
import com.jishop.dto.Step2Request;
import org.springframework.http.ResponseEntity;

public interface UserController {

    ResponseEntity<String> step1(Step1Request request);
    ResponseEntity<String> step2(Step2Request request);
}
