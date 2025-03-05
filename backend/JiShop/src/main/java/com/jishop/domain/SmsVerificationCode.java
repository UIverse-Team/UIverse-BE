package com.jishop.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class SmsVerificationCode {

    @Id
    private String token;
    private String phonenumber;
    private String code;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    @Builder
    public SmsVerificationCode(String token, String phonenumber, String code, LocalDateTime expiresAt) {
        this.token = token;
        this.phonenumber = phonenumber;
        this.code = code;
        this.expiresAt = expiresAt;
    }
}

