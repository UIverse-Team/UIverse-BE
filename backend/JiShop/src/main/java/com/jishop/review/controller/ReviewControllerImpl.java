package com.jishop.review.controller;

import com.jishop.review.dto.*;
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
    public ResponseEntity<PagedModel<?>> getProductReview(@RequestParam(value = "userId", required = false) Long userId,
                                                          @PathVariable("productId") Long productId,
                                                          @PageableDefault(size = 15, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        //todo: 추가사항
        // 1. sort 값에 따른 필터링 기능 구현 및 검증..

        if (userId == null) {
            PagedModel<ReviewWithOutUserResponse> productReviews = reviewService.getProductReviewsWithoutUser(productId, pageable);
            return ResponseEntity.ok(productReviews);
        }

        PagedModel<ReviewWithUserResponse> productReviewsWithUser = reviewService.getProductReviewsWithUser(productId, userId, pageable);

        return ResponseEntity.ok(productReviewsWithUser);
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
    @PostMapping("{reviewId}/likes")
    public ResponseEntity<String> likeReview(@PathVariable(value = "reviewId") Long reviewId,
                                        @RequestBody @Valid LikerIdRequest likerIdRequest) {

        reviewService.likeReview(likerIdRequest, reviewId);

        return ResponseEntity.ok("리뷰 좋아요 - 성공");
    }

    @Override
    @DeleteMapping("{reviewId}/unlikes")
    public ResponseEntity<String> unlikeReview(@PathVariable(value = "reviewId") Long reviewId,
                                               @RequestBody @Valid LikerIdRequest likerIdRequest) {

        reviewService.unlikeReview(likerIdRequest, reviewId);

        return ResponseEntity.ok("리뷰 좋아요 취소 - 성공");
    }

    @Override
    @PatchMapping("/{reviewId}/delete")
    public ResponseEntity<String> deleteReview(@PathVariable("reviewId") Long reviewId,
                                               //사용자 아이디
                                               @RequestParam("userId") Long userId) {

        reviewService.deleteReview(reviewId, userId);

        return ResponseEntity.ok("리뷰 삭제 - 성공");
    }

    @Override
    @PatchMapping("/{reviewId}")
    public ResponseEntity<String> updateReview(@PathVariable("reviewId") Long reviewId,
                                               // 사용자 아이디
                                               @RequestParam("userId") Long userId,
                                               @RequestBody UpdateReviewRequest updateReviewRequest){

        reviewService.updateReview(reviewId,userId,updateReviewRequest);

        return ResponseEntity.ok("리뷰 수정 - 성공");
    }
}
