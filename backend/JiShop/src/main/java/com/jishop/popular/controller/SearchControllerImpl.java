package com.jishop.popular.controller;

import com.jishop.popular.dto.SearchRequest;
import com.jishop.popular.service.SearchService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchControllerImpl implements SearchController {

    private final SearchService searchService;

    @Override
    @PostMapping
    public ResponseEntity<Void> search(@RequestBody SearchRequest searchRequest, HttpServletRequest servletRequest) {
        String clientIp = servletRequest.getRemoteAddr();
        boolean processedResult = searchService.processSearch(searchRequest.keyword(), clientIp);
        if(!processedResult){
            log.info("유효하지 않은 검색어 입력: {}", searchRequest.keyword());
        }

        return ResponseEntity.ok().build();
    }
}