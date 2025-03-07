package com.jishop.controller.impl;

import com.jishop.controller.FaqController;
import com.jishop.dto.FaqResponse;
import com.jishop.service.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/faqs")
public class FaqControllerImpl implements FaqController {

    private final FaqService faqService;

    @Override
    @GetMapping
    public List<FaqResponse> getAllFaqs() {
        return faqService.getAllFaqs();
    }
}