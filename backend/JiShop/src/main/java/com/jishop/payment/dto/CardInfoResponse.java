package com.jishop.payment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jishop.payment.domain.CardInfo;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CardInfoResponse(
        String issuerCode,
        String acquirerCode,
        String number,
        int installmentPlanMonths,
        boolean interestFree,
        String approveNo,
        boolean useCardPoint,
        String cardType,
        String ownerType,
        String acquireStatus,
        int amount
) {
    public static CardInfoResponse fromCardInfo(CardInfo cardInfo){
        return new CardInfoResponse(
                cardInfo.getIssuerCode(),
                cardInfo.getAcquirerCode(),
                cardInfo.getNumber(),
                cardInfo.getInstallmentPlanMonths(),
                cardInfo.isInterestFree(),
                cardInfo.getApproveNo(),
                cardInfo.isUseCardPoint(),
                cardInfo.getCardType(),
                cardInfo.getOwnerType(),
                cardInfo.getAcquireStatus(),
                cardInfo.getAmount()
        );
    }
}
