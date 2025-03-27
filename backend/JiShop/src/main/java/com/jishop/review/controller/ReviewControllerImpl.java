package com.jishop.review.controller;

import com.jishop.common.response.SliceResponse;
import com.jishop.member.annotation.CurrentUser;
import com.jishop.member.domain.User;
import com.jishop.review.dto.*;
import com.jishop.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewControllerImpl implements ReviewController {

    private final ReviewService reviewService;

    @Override
    @PostMapping
    public ResponseEntity<Long> createReview(@CurrentUser User user,
                                             @RequestBody @Valid ReviewRequest reviewRequest) {

        return ResponseEntity.ok(reviewService.createReview(reviewRequest, user));
    }

    @Override
    @GetMapping("/products/{productId}")
    public ResponseEntity<PagedModel<?>> getProductReview(@CurrentUser User user,
                                                          @PathVariable("productId") Long productId,
                                                          @PageableDefault(size = 15, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        //todo: sort 값에 따른 필터링 기능 구현 및 검증..
        if (user == null) return ResponseEntity.ok(reviewService.getProductReviews(productId, pageable));

        return ResponseEntity.ok(reviewService.getProductReviews(productId, user.getId(), pageable));
    }

    @Override
    @GetMapping("/mypage")
    public ResponseEntity<PagedModel<MyPageReviewResponse>> getMyPageReview(
            @CurrentUser User user,
            @PageableDefault(size = 15, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        //todo: sort 값에 따른 필터링 기능 구현 및 검증..
        return ResponseEntity.ok(reviewService.getMyPageReviews(user.getId(), pageable));
    }

    /**
     * 마이페이지 쓰기 가능한 리뷰 보기
     * @param user
     * @param pageable
     * @return
     */
    @Override
    @GetMapping("/mypage/write")
    public ResponseEntity<PagedModel<ReviewWriteResponse>> getMyPageReviewWrite(
            @CurrentUser User user,
            @PageableDefault(size = 15, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        return ResponseEntity.ok(reviewService.getMyPageReviewWrite(user, pageable));
    }

    @Override
    @GetMapping("/images")
    public ResponseEntity<SliceResponse<ReviewImageResponse>> getReviewImages(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Slice<ReviewImageResponse> reviewImages = reviewService.getReviewImages(pageable);

        return ResponseEntity.ok(SliceResponse.from(reviewImages));
    }

    @Override
    @GetMapping("{reviewId}/detail")
    public ResponseEntity<?> getDetailReview(@PathVariable("reviewId") Long reviewId,
                                             @CurrentUser User user) {
        if (user == null) return ResponseEntity.ok(reviewService.getDetailReview(reviewId));

        return ResponseEntity.ok(reviewService.getDetailReview(reviewId, user));
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
                                               @RequestBody @Valid UpdateReviewRequest updateReviewRequest) {
        reviewService.updateReview(reviewId, userId, updateReviewRequest);

        return ResponseEntity.ok("리뷰 수정 - 성공");
    }
}
