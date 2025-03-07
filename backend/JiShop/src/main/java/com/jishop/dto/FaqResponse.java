package com.jishop.dto;

import com.jishop.domain.Faq;
import com.jishop.domain.Notice;
import lombok.Getter;

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