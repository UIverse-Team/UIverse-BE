package com.jishop.service;

import com.jishop.dto.NoticeDetailResponse;
import com.jishop.dto.NoticeResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

public interface NoticeService {

    // 공지사항 전체 목록 조회
    public PagedModel<NoticeResponse> getAllNotices(Pageable pageable);
    // 공지사항 상세 조회
    public NoticeDetailResponse getNotice(Long id);
}