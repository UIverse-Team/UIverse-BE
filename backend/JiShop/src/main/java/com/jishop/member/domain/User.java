package com.jishop.member.domain;

import com.jishop.common.util.BaseEntity;
import com.jishop.member.dto.request.RecoveryPWRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity implements Serializable {

    @Column(nullable = false, name = "login_id")
    private String loginId;         // 회원 이메일, 소셜 로그인시 id 저장
    private String password;        // 비밀번호, null 저장
    private String name;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LoginType provider;     // 로그인 타입 저장

    // 성별, 연령, 휴대전화 번호 추가 예정
    private String birthDate;
    private String gender;
    private String phone;

    // 주소 추가 예정

    //소셜 회원가입
    @Builder
    public User(String loginId, String password, String name, String birthDate, String gender, String phone, LoginType provider) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phone = phone;
        this.provider = provider;
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}
