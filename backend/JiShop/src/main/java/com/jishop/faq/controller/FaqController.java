package com.jishop.faq.controller;

import com.jishop.faq.dto.FaqResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;

@Tag(name = "FAQ API - 보류")
public interface FaqController {

    ResponseEntity<PagedModel<FaqResponse>> getAllPopularFaqs(Pageable pageable);
}