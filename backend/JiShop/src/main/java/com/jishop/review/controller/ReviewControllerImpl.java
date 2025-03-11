package com.jishop.review.controller;

import com.jishop.review.dto.ReviewRequest;
import com.jishop.review.dto.ReviewResponse;
import com.jishop.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewControllerImpl implements ReviewController {

    private final ReviewService reviewService;

    @Override
    @PostMapping
    public ResponseEntity<Long> craeteReview(@RequestBody ReviewRequest reviewRequest) {
        //todo:
        // 1. imageService 만들어서 받기
        // 2. userId 세션에서 받아오기.
        Long userId = 1L;
        List<String> images = new ArrayList<>();
        Long reviewId = reviewService.createReview(reviewRequest, images, userId);

        return ResponseEntity.ok(reviewId);
    }

    @GetMapping("/products/{saleProductId}")
    @Override
    public ResponseEntity<PagedModel<ReviewResponse>> getProdcutReview(@PathVariable("saleProductId") Long saleProductId,
                                                                       @PageableDefault(size = 15, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        PagedModel<ReviewResponse> productReviews = reviewService.getProductReviews(saleProductId, pageable);

        return ResponseEntity.ok(productReviews);
    }


}
