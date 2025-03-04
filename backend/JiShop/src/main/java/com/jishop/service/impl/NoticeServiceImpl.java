package com.jishop.service.impl;

import com.jishop.dto.NoticeDetailResponse;
import com.jishop.dto.NoticeResponse;
import com.jishop.repository.NoticeRepository;
import com.jishop.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    @Transactional(readOnly = true)
    public PagedModel<NoticeResponse> getAllNotices(Pageable pageable) {
        return new PagedModel<>(noticeRepository.findAll(pageable)
                .map(NoticeResponse::from));
    }

    @Override
    @Transactional(readOnly = true)
    public NoticeDetailResponse getNotice(Long id) {
        return NoticeDetailResponse.from(noticeRepository.findOne(id));
    }

    @Override
    public PagedModel<NoticeResponse> searchNotices(String keyword, Pageable pageable) {
        if(keyword == null || keyword.isBlank()){
            return new PagedModel<>(Page.empty());
        }

        return new PagedModel<>(noticeRepository.searchByKeyword(keyword, pageable)
                .map(NoticeResponse::from));
    }
}
