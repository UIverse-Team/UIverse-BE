package com.jishop.payment.domain;

/**
 * 결제 수단
 */
public enum PaymentMethod {
    CARD,               // 카드 결제
    VIRTUAL_ACCOUNT,    // 가상계좌
    SIMPLE_PAYMENT,     // 간편결제
    MOBILE,             // 휴대폰 결제
    BANK_TRANSFER,      // 계좌이체
    CULTURE_GIFT_CARD,  // 문화상품권
    BOOK_GIFT_CARD,     // 도서문화상품권
    GAME_GIFT_CARD      // 게임문화상품권
}
