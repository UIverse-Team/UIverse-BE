package com.jishop.popular.controller;

import com.jishop.popular.dto.PopularKeywordResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "급상승 키워드와 연관 상품 조회 API")
public interface PopularController {

    @Operation(
            summary = "쇼핑몰 메인 페이지에서 급상승 키워드와 연관 상품 리스트 조회",
            description = "1시간마다 갱신되는 급상승 키워드와 연관 상품 리스트를 반환"
    )
    PopularKeywordResponse getTop5PopularKeywordsAndProducts();

    @Operation(
            summary = "쇼핑몰 랭킹 디테일 페이지에서 급상승 키워드와 연관 상품 리스트 조회",
            description = "1시간마다 갱신되는 급상승 키워드와 연관 상품 리스트를 반환"
    )
    PopularKeywordResponse getTop10PopularKeywordsAndProducts();
}
