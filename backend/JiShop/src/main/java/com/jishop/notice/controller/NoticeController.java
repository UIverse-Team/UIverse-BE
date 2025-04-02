package com.jishop.notice.controller;

import com.jishop.notice.dto.NoticeDetailResponse;
import com.jishop.notice.dto.NoticeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;

@Tag(name = "공지사항 API")
public interface NoticeController {

    @Operation(summary = "공지사항 전체 조회", description = "공지사항 전체 목록을 조회")
    ResponseEntity<PagedModel<NoticeResponse>> getAllNotices(Pageable pageable);

    @Operation(summary = "공지사항 조회", description = "공지사항 ID로 특정 공지사항 정보를 조회")
    ResponseEntity<NoticeDetailResponse> getNotice(Long id);

    @Operation(summary = "공지사항 검색", description = "검색어를 통해 공지사항을 검색")
    ResponseEntity<PagedModel<NoticeResponse>> searchNotices(String keyword, Pageable pageable);
}