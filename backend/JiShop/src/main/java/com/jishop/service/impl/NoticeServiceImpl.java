package com.jishop.service.impl;

import com.jishop.dto.NoticeResponse;
import com.jishop.repository.NoticeRepository;
import com.jishop.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;

    @Override
    public PagedModel<NoticeResponse> getAllNotices(Pageable pageable) {
        return new PagedModel<>(noticeRepository.findAll(pageable)
                .map(NoticeResponse::from));
    }
}
