package com.jishop.payment.domain;

/**
 * 결제 수단
 */
public enum PaymentMethod {
    CARD("카드"),
    SIMPLE_PAYMENT("간편결제");
//    VIRTUAL_ACCOUNT(가상 계좌)
//    MOBILE("휴대폰결제")
//    BANK_TRANSFER("계좌이체")
//    CULTURE_GIFT_CARD("문화상품권")
//    BOOK_GIFT_CARD("도서문화상품권")
//    GAME_GIFT_CARD("게임문화상품권")

    private final String value;

    PaymentMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PaymentMethod fromValue(String value) {
        for (PaymentMethod method : values()) {
            if (method.value.equals(value)) {
                return method;
            }
        }
        throw new IllegalArgumentException("Unknown method: " + value);
    }
}
