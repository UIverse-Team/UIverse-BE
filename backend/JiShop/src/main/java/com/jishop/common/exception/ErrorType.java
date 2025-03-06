package com.jishop.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {

    // USER
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "유효하지 않는 인증 코드입니다!"),

    // NOTICE
    NOTICE_NOT_FOUND(HttpStatus.NOT_FOUND, "공지사항이 존재하지 않습니다."),

    // CERTIFICATION
    EMAIL_SEND_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "EMAIL 전송에 실패했습니다."),
    SMS_SEND_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "SMS 전송에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
