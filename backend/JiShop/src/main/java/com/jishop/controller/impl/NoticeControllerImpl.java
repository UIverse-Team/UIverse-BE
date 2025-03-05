package com.jishop.controller.impl;

import com.jishop.controller.NoticeController;
import com.jishop.dto.NoticeDetailResponse;
import com.jishop.dto.NoticeResponse;
import com.jishop.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notices")
public class NoticeControllerImpl implements NoticeController {

    private final NoticeService noticeService;

    @Override
    @GetMapping
    public ResponseEntity<PagedModel<NoticeResponse>> getAllNotices(
            @PageableDefault(size = 15, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(noticeService.getAllNotices(pageable));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<NoticeDetailResponse> getNotice(@PathVariable Long id) {
        return ResponseEntity.ok().body(noticeService.getNotice(id));
    }

    @Override
    @GetMapping("/search")
    public ResponseEntity<PagedModel<NoticeResponse>> searchNotices(
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 15, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(noticeService.searchNotices(keyword, pageable));
    }
}