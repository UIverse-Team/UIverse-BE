package com.log.controller;

import com.log.dto.PageViewRequest;
import com.log.dto.ProductClickRequest;
import com.log.dto.ReviewRequest;
import com.log.dto.SearchRequest;

public interface LogController {

    Long addPageLog(PageViewRequest request);
    void updatePage(PageViewRequest request, Long id);
    void addSearch(SearchRequest requst);
    void addProductClick(ProductClickRequest requst);
    void addSearch(ReviewRequest requst);
}
