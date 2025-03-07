package com.jishop.dto;

import com.jishop.domain.EmailCertification;

import java.time.LocalDateTime;

public record EmailRequest(
        String email
) {
    public EmailCertification toEntity(String token, String cerfitication){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expriseAt = now.plusMinutes(5);

        return EmailCertification.builder()
                .token(token)
                .email(email)
                .certificationCode(cerfitication)
                .createdAt(now)
                .expriesAt(expriseAt)
                .build();
    }
}
