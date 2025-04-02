package com.jishop.faq.service;

import com.jishop.faq.dto.FaqResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

public interface FaqService {

    PagedModel<FaqResponse> getAllPopularFaqs(Pageable pageable);
}