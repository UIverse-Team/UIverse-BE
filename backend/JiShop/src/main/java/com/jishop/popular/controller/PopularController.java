package com.jishop.popular.controller;

import com.jishop.popular.dto.PopularKeywordResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "인기 검색어 조회 API")
public interface PopularController {
    PopularKeywordResponse getPopularKeywordAndProduct();
}
