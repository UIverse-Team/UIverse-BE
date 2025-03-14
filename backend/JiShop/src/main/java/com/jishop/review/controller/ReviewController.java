package com.jishop.review.controller;

import com.jishop.review.dto.MyPageReviewResponse;
import com.jishop.review.dto.ReviewRequest;
import com.jishop.review.dto.ReviewResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface ReviewController {

    ResponseEntity<Long> craeteReview(ReviewRequest reviewRequest);

    ResponseEntity<PagedModel<ReviewResponse>> getProdcutReview(Long saleProductId, Pageable pageable);

    ResponseEntity<PagedModel<MyPageReviewResponse>> getMyPageReview(Pageable pageable);
}
