package com.jishop.payment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jishop.payment.domain.EasyPayInfo;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record EasyPayInfoResponse(
        String provider,
        int amount,
        int discountAmount
) {
    public static EasyPayInfoResponse fromEasyPayInfo(EasyPayInfo easyPayInfo) {
        return new EasyPayInfoResponse(
                easyPayInfo.getProvider().name(),
                easyPayInfo.getAmount(),
                easyPayInfo.getDiscountAmount()
        );
    }
}
