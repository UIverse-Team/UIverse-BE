package com.jishop.dto;

import com.jishop.domain.SmsCertification;

import java.time.LocalDateTime;

public record SmsRequest(
        String phoneNumber
){
    public SmsCertification toEntity(String token, String code){
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime expriseAt = createdAt.plusMinutes(5);

        return  SmsCertification.builder()
                .phonenumber(phoneNumber)
                .token(token)
                .code(code)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .build();

    }
}