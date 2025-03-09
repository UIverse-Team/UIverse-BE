package com.jishop.faq.dto;

import com.jishop.faq.domain.Faq;

public record FaqResponse(
        String category,
        String title,
        String content
) {
    public static FaqResponse from(Faq faq) {
        return new FaqResponse(
                faq.getCategory().name(),
                faq.getTitle(),
                faq.getContent()
        );
    }
}