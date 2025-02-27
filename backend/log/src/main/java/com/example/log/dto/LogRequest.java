package com.example.log.dto;

import com.example.log.domain.Log;
import com.example.log.domain.enums.UserActionType;

public record LogRequest(
        Long userId,
        String message,
        UserActionType userActionType
) {
    public Log toEntity() {
        return new Log(userId, message, userActionType);
    }
}
