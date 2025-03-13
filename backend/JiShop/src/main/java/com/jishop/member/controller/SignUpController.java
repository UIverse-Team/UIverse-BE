package com.jishop.member.controller;

import com.jishop.member.dto.FinalStepRequest;
import com.jishop.member.dto.Step1Request;
import com.jishop.member.dto.Step2Request;
import com.jishop.member.dto.request.FindUserRequest;
import com.jishop.member.dto.response.FindUserResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "로컬 회원가입 API")
public interface SignUpController {

    ResponseEntity<String> step1(Step1Request request);
    ResponseEntity<String> step2(Step2Request request);
    ResponseEntity<String> lastStep(FinalStepRequest request);
}
