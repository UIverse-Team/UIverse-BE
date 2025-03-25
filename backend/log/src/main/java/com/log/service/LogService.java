package com.log.service;

import com.log.dto.PageViewRequest;
import com.log.dto.ProductClickRequest;
import com.log.dto.ReviewRequest;
import com.log.dto.SearchRequest;

public interface LogService {

    Long addPageLog(PageViewRequest pageRequest);

    void updatePageLog(PageViewRequest pageRequest, Long id);

    void addSearchLog(SearchRequest request);

    void addProductClick(ProductClickRequest request);

    void addReviewLog(ReviewRequest request);
}
