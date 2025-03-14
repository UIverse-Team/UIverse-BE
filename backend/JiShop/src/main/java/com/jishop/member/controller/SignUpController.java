package com.jishop.member.controller;

import com.jishop.member.dto.request.FinalStepRequest;
import com.jishop.member.dto.request.Step1Request;
import com.jishop.member.dto.request.Step2Request;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

@Tag(name = "로컬 회원가입 API")
public interface SignUpController {

    ResponseEntity<String> step1(Step1Request request, HttpServletResponse response);
    ResponseEntity<String> step2(Step2Request request, HttpServletRequest servletRequest, HttpServletResponse response);
    ResponseEntity<String> lastStep(FinalStepRequest request, HttpServletRequest servletRequest,HttpServletResponse response);
}
