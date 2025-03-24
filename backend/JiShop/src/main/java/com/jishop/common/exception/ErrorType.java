package com.jishop.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {

    // COMMON
    DATA_ALREADY_DELETED(HttpStatus.GONE, "이미 삭제된 데이터 입니다."),
    MATCH_NOT_USER(HttpStatus.NOT_FOUND, "해당 권한이 없는 유저입니다."),
    TYPE_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 접근 타입이 잘 못 되었습니다."),

    // USER
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    EMAIL_DUPLICATE(HttpStatus.CONFLICT, "이미 존재하는 이메일 입니다."),
    PASSWORD_EXISTS(HttpStatus.BAD_REQUEST, "현재 비밀번호화 동일합니다."),

    PROVIDER_NOT_FOUND(HttpStatus.BAD_REQUEST, "지원하지 않는 소셜 로그인 입니다!"),
    AUTHORIZATION_CODE_NOT_FOUND(HttpStatus.BAD_REQUEST,"코드의 유효기간이 만료됐거나 존재하지 않습니다."),

    // REVIEW
    REVIEW_DUPLICATE(HttpStatus.CONFLICT, "이미 리뷰를 작성했습니다."),
    RATING_OUT_OF_RANGE(HttpStatus.BAD_REQUEST, "별점은 1~5점을 해야한다."),
    REVIEW_NOT_FOUND(HttpStatus.BAD_REQUEST, "작성하신 리뷰가 없습니다."),
    // NOTICE
    NOTICE_NOT_FOUND(HttpStatus.NOT_FOUND, "공지사항이 존재하지 않습니다."),

    // CERTIFICATION
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "유효하지 않는 인증 코드입니다!"),
    EMAIL_SEND_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "EMAIL 전송에 실패했습니다."),
    SMS_SEND_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "SMS 전송에 실패했습니다."),

    // PRODUCT
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),
    STOCK_NOT_FOUND(HttpStatus.NOT_FOUND, "재고 정보를 찾을 수 없습니다."),

    // Category
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "카테고리를 찾을 수 없습니다."),
    CATEGORY_RESULT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 카테고리에 대한 결과가 없습니다."),

    // ORDER
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다"),
    ORDER_DETAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "주문상세를 찾을 수 없습니다"),
    ORDER_NUMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문 번호를 찾을 수 없습니다"),
    ORDER_ALREADY_CONFIRMED(HttpStatus.BAD_REQUEST, "이미 구매 확정된 주문입니다."),
    ORDER_ALREADY_CANCELED(HttpStatus.BAD_REQUEST, "주문이 이미 취소되었습니다."),
    ORDER_CANCEL_FAILED(HttpStatus.BAD_REQUEST,"주문 취소 중 오류가 발생했습니다"),
    ORDER_CANNOT_CANCEL_AFTER_SHIPPING(HttpStatus.BAD_REQUEST, "배송이 시작한 이후에는 주문 취소를 할 수 없습니다"),

    //CART
    CART_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "장바구니 상품을 찾을 수 없습니다."),

    // VALIDATION
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "값을 잘못 입력했습니다."),

    // STOCK
    INSUFFICIENT_STOCK(HttpStatus.BAD_REQUEST, "재고가 부족합니다"),
    STOCK_OPERATION_FAILED(HttpStatus.CONFLICT, "재고 관리 중 오류가 발생했습니다"),
    INVALID_QUANTITY(HttpStatus.BAD_REQUEST, "재고의 값은 1이상이어야 합니다."),

    // STORE
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 스토어가 존재하지 않습니다"),

    // Address,
    DEFAULTADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "기본 배송지가 없습니다."),
    ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "없는 배송지 입니다."),
    DEFAULT_ADDRESS_REQUIRED(HttpStatus.NOT_FOUND, "기본 배송지가 꼭 필요합니다."),

    //REDISSON
    LOCK_ACQUISITION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "주문 저리 중 잠금 획득에 실패했습니다. 잠시 후 시도해주세요"),
    CONCURRENT_ORDER_PROCESSING(HttpStatus.INTERNAL_SERVER_ERROR, "동시에 너무 많은 주문이 처리되고 있습니다. 잠시 후 다시 시도해주세요");

    private final HttpStatus httpStatus;
    private final String message;
}
