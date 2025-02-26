package com.jishop.domain;

public enum NoticePriority {

    // 긴급 - 시스템 장애 및 서비스 중단, 개인정보 유출 등 긴급한 공지사항
    EMERGENCY,
    // 중요 - 서비스 점검, 정책 변경사항 등 서비스 이용에 영향을 미치는 변경사항
    IMPORTANT,
    // 일반 - 일반적인 공지사항
    NORMAL
}
