package com.jishop.popular.controller;

import com.jishop.popular.dto.SearchRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

@Tag(name = "검색어 입력 요청 API")
public interface SearchController {

    @Operation(
            summary = "사용자 검색어 입력 요청 처리",
            description = "사용자가 검색어를 입력하면, 검색어의 유효성을 검증 후 저장"
    )
    ResponseEntity<Void> search(SearchRequest searchRequest, HttpServletRequest servletRequest);
}
