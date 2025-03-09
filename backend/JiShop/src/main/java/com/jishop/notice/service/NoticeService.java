package com.jishop.notice.service;

import com.jishop.notice.dto.NoticeDetailResponse;
import com.jishop.notice.dto.NoticeResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

public interface NoticeService {

    PagedModel<NoticeResponse> getAllNotices(Pageable pageable);
    NoticeDetailResponse getNotice(Long id);
    PagedModel<NoticeResponse> searchNotices(String keyword, Pageable pageable);
}