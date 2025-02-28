package com.example.log.domain.enums;

public enum UserActionType {
    PAGE_VIEW,          // 페이지 방문
    BUTTON_CLICK,       // 버튼 클릭
    LINK_CLICK,         // 링크 클릭
    SCROLL,            // 스크롤 이벤트
    HOVER,             // 마우스 오버
    FORM_SUBMIT,       // 폼 제출
    VIDEO_PLAY,        // 동영상 재생
    VIDEO_PAUSE,       // 동영상 일시정지
    VIDEO_COMPLETE,    // 동영상 끝까지 시청
    TEXT_INPUT         // 텍스트 입력
}