package com.jishop.payment.domain;

public enum PaymentStatus {
    READY,                  // 결제 생성 시 초기 상태, 인증 전까지 유지
    IN_PROGRESS,            // 결제수단 정보 및 소유자 인증 완료
    DONE,                   // 인증된 결제수단으로 요쳥 결제 승인 완료
    CANCELED,               // 승인된 결제가 취소된 상태
    PARTIAL_CANCELED,       // 승인된 결제가 부분 취소된 상태
    ABORTED,                // 결제 승인이 실패한 상태
    EXPIRED                 // 결제 유효시간(30분) 초과로 결제 취소
}
