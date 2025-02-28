package com.example.log.domain;

import com.example.log.common.util.BaseEntity;
import com.example.log.domain.enums.UserActionType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Log extends BaseEntity {

    private Long userId;
    private String message;
    @Enumerated(EnumType.STRING)
    private UserActionType userActionType;

    public Log(Long userId, String message, UserActionType userActionType) {
        this.userId = userId;
        this.message = message;
        this.userActionType = userActionType;
    }
}
