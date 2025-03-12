package com.jishop.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailCertification {

    @Id
    private String token;
    // 인증 번호 발송 받을 email
    @Column(nullable = false)
    private String email;
    // 인증 코드
    @Column(nullable = false)
    private String certificationCode;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

    @Builder
    public EmailCertification(String token, String email, String certificationCode,
                              LocalDateTime createdAt, LocalDateTime expriesAt) {
        this.token = token;
        this.email = email;
        this.certificationCode = certificationCode;
        this.createdAt = createdAt;
        this.expiresAt = expriesAt;
    }
}
