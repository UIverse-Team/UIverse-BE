package com.jishop.faq.controller;

import com.jishop.faq.dto.FaqResponse;
import com.jishop.faq.service.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/faqs")
public class FaqControllerImpl implements FaqController {

    private final FaqService faqService;

    @Override
    @GetMapping
    public ResponseEntity<PagedModel<FaqResponse>> getAllPopularFaqs(
            @PageableDefault(size = 15, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(faqService.getAllPopularFaqs(pageable));
    }
}