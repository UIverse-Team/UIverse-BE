package com.jishop.payment.domain;

/**
 * 결제 처리 상태
 */
public enum PaymentStatus {
    READY,                  // 결제 초기 상태
    IN_PROGRESS,            // 결제 인증 완료
    WAITING_FOR_DEPOSIT,    // 가상계좌에서 구매자의 입금 대기 상태
    DONE,                   // 결제 승인 완료
    CANCELLED,              // 승인된 결제 취소
    PARTICAL_CANCELLED,     // 승인된 결제 부분 취소
    ABORTED,                // 결제 승인 실패
    EXPIRED                 // 결제 유효시간 만료로 거래 취소
}
