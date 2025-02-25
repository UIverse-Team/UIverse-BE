package com.jishop.controller.impl;

import com.jishop.controller.NoticeController;
import com.jishop.dto.NoticeResponse;
import com.jishop.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notices")
public class NoticeControllerImpl implements NoticeController {

    private final NoticeService noticeService;

    @Override
    @GetMapping
    public PagedModel<NoticeResponse> getAllNotices(
            @PageableDefault(page = 0, size = 15,sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return noticeService.getAllNotices(pageable);
    }
}