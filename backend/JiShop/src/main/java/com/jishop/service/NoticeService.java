package com.jishop.service;

import com.jishop.dto.NoticeDetailResponse;
import com.jishop.dto.NoticeResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

public interface NoticeService {

    PagedModel<NoticeResponse> getAllNotices(Pageable pageable);
    NoticeDetailResponse getNotice(Long id);
    PagedModel<NoticeResponse> searchNotices(String keyword, Pageable pageable);
}