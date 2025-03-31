package com.jishop.payment.domain;

public enum PaymentProvider {
    TOSS_PAY("토스페이"),
    KAKAO_PAY("카카오페이"),
    NAVER_PAY("네이버페이");

    private final String value;

    PaymentProvider(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PaymentProvider fromValue(String value) {
        for (PaymentProvider provider : values()) {
            if (provider.value.equals(value)) {
                return provider;
            }
        }
        throw new IllegalArgumentException("Unknown provider: " + value);
    }
}
