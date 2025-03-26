package com.jishop.popular.controller;

import com.jishop.popular.dto.SearchRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

@Tag(name = "검색어 입력 요청 API")
public interface SearchController {

    ResponseEntity<Void> search(SearchRequest searchRequest, HttpServletRequest servletRequest);
}
