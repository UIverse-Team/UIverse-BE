package com.jiseller.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {

    // PRODUCT
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 상품을 찾을 수 없습니다"),

    // CATEGORY
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "카테고리를 찾을 수 없습니다."),

    // OPTION
    OPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 옵션을 찾을 수 없습니다"),

    // STORE
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 스토어가 존재하지 않습니다"),

    // STOCK
    INSUFFICIENT_STOCK(HttpStatus.BAD_REQUEST, "재고가 부족합니다"),
    STOCK_OPERATION_FAILED(HttpStatus.CONFLICT, "재고 관리 중 오류가 발생했습니다"),
    INVALID_QUANTITY(HttpStatus.BAD_REQUEST, "재고의 값은 1이상이어야 합니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
