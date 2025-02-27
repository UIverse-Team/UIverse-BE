package com.jishop.controller;

import com.jishop.dto.NoticeResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

@Tag(name = "공지사항 API")
public interface NoticeController {

    public PagedModel<NoticeResponse> getAllNotices(Pageable pageable);
}