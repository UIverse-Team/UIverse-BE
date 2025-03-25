package com.jishop.popular.controller;

import com.jishop.popular.dto.SearchRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

@Tag(name = "검색 요청 API")
public interface SearchController {

    ResponseEntity<?> logSearch(SearchRequest searchRequest, HttpServletRequest servletRequest);
}
