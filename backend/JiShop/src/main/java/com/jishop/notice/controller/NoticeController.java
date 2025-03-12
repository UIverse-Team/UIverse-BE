package com.jishop.notice.controller;

import com.jishop.notice.dto.NoticeDetailResponse;
import com.jishop.notice.dto.NoticeResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;

@Tag(name = "공지사항 API")
public interface NoticeController {

    ResponseEntity<PagedModel<NoticeResponse>> getAllNotices(Pageable pageable);
    ResponseEntity<NoticeDetailResponse> getNotice(Long id);
    ResponseEntity<PagedModel<NoticeResponse>> searchNotices(String keyword, Pageable pageable);
}