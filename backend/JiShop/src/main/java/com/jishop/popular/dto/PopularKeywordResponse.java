package com.jishop.popular.dto;

import java.util.List;

public record PopularKeywordResponse(
        String time,
        List<PopularReponse> keywords
) {
}
