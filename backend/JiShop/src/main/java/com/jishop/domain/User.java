package com.jishop.domain;

import com.jishop.common.util.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@NoArgsConstructor
public class User extends BaseEntity {

    @Column(nullable = false)
    private String loginId;
    private String password;
    @Column
    private String name;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LoginType provider;

    @Builder
    public User(String loginId, String name, String password, LoginType provider) {
        this.loginId = loginId;
        this.name = name;
        this.password = password;
        this.provider = provider;
    }
}
