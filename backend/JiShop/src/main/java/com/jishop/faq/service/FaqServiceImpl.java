package com.jishop.faq.service;

import com.jishop.faq.dto.FaqResponse;
import com.jishop.faq.repository.FaqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FaqServiceImpl implements FaqService {

    private final FaqRepository faqRepository;

    @Override
    @Transactional(readOnly = true)
    public PagedModel<FaqResponse> getAllPopularFaqs(Pageable pageable) {
        return new PagedModel<>(faqRepository.findByIsPopularTrue(pageable)
                .map(FaqResponse::from));
    }
}