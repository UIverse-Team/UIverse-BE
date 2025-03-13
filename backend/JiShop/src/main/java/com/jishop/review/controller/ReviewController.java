package com.jishop.review.controller;

import com.jishop.review.dto.ReviewRequest;
import com.jishop.review.dto.ReviewResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;

public interface ReviewController {

    ResponseEntity<Long> craeteReview(ReviewRequest reviewRequest);
    ResponseEntity<PagedModel<ReviewResponse>> getProdcutReview(Long saleProductId, Pageable pageable);
}
