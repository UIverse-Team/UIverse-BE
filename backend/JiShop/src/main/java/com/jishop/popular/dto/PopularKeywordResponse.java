package com.jishop.popular.dto;

import java.util.List;

public record PopularKeywordResponse(
        String date,
        String time,
        List<PopularKeywordAndProduct> keywords
) {
}
