package com.jishop.domain;

import com.jishop.common.util.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Column(nullable = false, name = "login_id")
    private String loginId;         // 회원 이메일, 소셜 로그인시 id 저장
    private String password;        // 비밀번호, null 저장
    @Column
    private String name;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LoginType provider;     // 로그인 타입 저장
    // 성별, 연령, 휴대전화 번호 추가 예정

    @Builder
    public User(String loginId, String name, String password, LoginType provider) {
        this.loginId = loginId;
        this.name = name;
        this.password = password;
        this.provider = provider;
    }

    @Builder
    public User(String loginId, String password, LoginType provider) {
        this.loginId = loginId;
        this.password = password;
        this.provider = provider;
    }
}
