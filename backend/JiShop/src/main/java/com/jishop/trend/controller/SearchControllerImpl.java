package com.jishop.trend.controller;

import com.jishop.trend.dto.SearchRequest;
import com.jishop.trend.service.SearchService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchControllerImpl implements SearchController {

    private final SearchService searchService;

    @Override
    @PostMapping("/log")
    public ResponseEntity<?> logSearch(@RequestBody SearchRequest searchRequest, HttpServletRequest servletRequest) {
        String clientIp = servletRequest.getRemoteAddr();
        boolean result = searchService.processSearch(searchRequest.keyword(), clientIp);

        return result ? ResponseEntity.ok("검색어 처리 완료")
                : ResponseEntity.badRequest().body("유효하지 않은 검색어");
    }
}
