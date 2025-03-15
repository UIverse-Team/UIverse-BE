package com.jishop.review.controller;

import com.jishop.review.dto.MyPageReviewResponse;
import com.jishop.review.dto.ReviewRequest;
import com.jishop.review.dto.ReviewResponse;
import com.jishop.review.service.ReviewService;
import jakarta.validation.Valid;
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
    public ResponseEntity<Long> craeteReview(@RequestBody @Valid ReviewRequest reviewRequest) {
        //todo:
        // 1. imageService 만들어서 받기
        // 2. userId 세션에서 받아오기.
        Long userId = 1L;
        List<String> images = new ArrayList<>();
        Long reviewId = reviewService.createReview(reviewRequest, images, userId);

        return ResponseEntity.ok(reviewId);
    }

    @Override
    @GetMapping("/products/{productId}")
    public ResponseEntity<PagedModel<ReviewResponse>> getProdcutReview(@PathVariable("productId") Long productId,
                                                                       @PageableDefault(size = 15, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        //todo: 추가사항
        // 1. sort 값에 따른 필터링 기능 구현 및 검증..
        PagedModel<ReviewResponse> productReviews = reviewService.getProductReviews(productId, pageable);

        return ResponseEntity.ok(productReviews);
    }

    @Override
    @GetMapping("/mypage")
    public ResponseEntity<PagedModel<MyPageReviewResponse>> getMyPageReview(
            @PageableDefault(size = 15, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        //todo: 추가사항
        // 1. sort 값에 따른 필터링 기능 구현 및 검증..
        // 2. userId 세션에서 받기
        Long userId = 1L;
        PagedModel<MyPageReviewResponse> productReviews = reviewService.getMyPageReviews(userId, pageable);

        return ResponseEntity.ok(productReviews);
    }

    @Override
    public ResponseEntity<?> likeReview() {
        return null;
    }

    @Override
    public ResponseEntity<?> unlikeReview() {
        return null;
    }

}
