package com.jishop.member.dto.request;

import com.jishop.member.domain.EmailCertification;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record EmailRequest(
        @Email
        @NotBlank(message = "이메일을 입력해 주세요!")
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
