package com.jishop.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {

    // USER
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    EMAIL_DUPLICATE(HttpStatus.CONFLICT, "이미 존재하는 이메일 입니다."),

    // NOTICE
    NOTICE_NOT_FOUND(HttpStatus.NOT_FOUND, "공지사항이 존재하지 않습니다."),

    // CERTIFICATION
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "유효하지 않는 인증 코드입니다!"),
    EMAIL_SEND_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "EMAIL 전송에 실패했습니다."),
    SMS_SEND_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "SMS 전송에 실패했습니다."),

    // PRODUCT
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),

    // ORDER
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND,"주문을 찾을 수 없습니다"),
    ORDER_ALREADY_CANCELED(HttpStatus.BAD_REQUEST, "주문이 이미 취소되었습니다."),
    ORDER_NUMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문 번호를 찾을 수 없습니다"),

    // VALIDATION
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "값을 잘못 입력했습니다."),

    // STOCK
    INSUFFICIENT_STOCK(HttpStatus.BAD_REQUEST, "재고가 부족합니다");

    private final HttpStatus httpStatus;
    private final String message;
}
