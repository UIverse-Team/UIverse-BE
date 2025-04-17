package com.log.controller;

import com.log.dto.PageViewRequest;
import com.log.dto.ProductClickRequest;
import com.log.dto.ReviewRequest;
import com.log.dto.SearchRequest;

public interface LogController {

    Long addPageLog(PageViewRequest request, Long userId);
    void updatePage(PageViewRequest request, Long id);
    void addSearch(SearchRequest requst, Long userId);
    void addProductClick(ProductClickRequest requst, Long userId);
    void addSearch(ReviewRequest requst, Long userId);
}
