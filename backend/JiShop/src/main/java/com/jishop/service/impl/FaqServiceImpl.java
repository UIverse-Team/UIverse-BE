package com.jishop.service.impl;

import com.jishop.dto.FaqResponse;
import com.jishop.repository.FaqRepository;
import com.jishop.service.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FaqServiceImpl implements FaqService {

    private final FaqRepository faqRepository;

    @Override
    @Transactional(readOnly = true)
    public List<FaqResponse> getAllFaqs() {
        return faqRepository.findAll()
                .stream()
                .map(FaqResponse::from)
                .toList();
    }
}