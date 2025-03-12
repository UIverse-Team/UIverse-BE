package com.jishop.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SmsCertification {

    @Id
    private String token;
    @Column(nullable = false)
    private String phonenumber;
    @Column(nullable = false)
    private String code;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    @Builder
    public SmsCertification(String token, String phonenumber, String code, LocalDateTime createdAt, LocalDateTime expiresAt) {
        this.token = token;
        this.phonenumber = phonenumber;
        this.code = code;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }
}

