package com.jishop.payment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardInfo {

    private String issuerCode;          // 카드 발급사 코드
    private String acquirerCode;        // 카드 매입사 코드
    private String number;              // 마스킹된 카드 번호
    private int installmentPlanMonths;  // 할부 개월 수
    private boolean isInterestFree;       // 무이자 여부
    private String approveNo;           // 승인 번호
    private boolean useCardPoint;       // 카드 포인트 사용 여부
    private String cardType;            // 카드 타입 - 신용, 체크
    private String ownerType;           // 소유자 유형 - 개인, 법인
    private String acquireStatus;       // 매입 상태

    @Column(name = "card_amount")
    private int amount;                 // 카드 결제 금액

    @Builder
    private CardInfo(String issuerCode, String acquirerCode, String number, int installmentPlanMonths,
                    boolean isInterestFree, String approveNo, boolean useCardPoint,
                    String cardType, String ownerType, String acquireStatus, int amount) {
        this.issuerCode = issuerCode;
        this.acquirerCode = acquirerCode;
        this.number = number;
        this.installmentPlanMonths = installmentPlanMonths;
        this.isInterestFree = isInterestFree;
        this.approveNo = approveNo;
        this.useCardPoint = useCardPoint;
        this.cardType = cardType;
        this.ownerType = ownerType;
        this.acquireStatus = acquireStatus;
        this.amount = amount;
    }
}
