package com.jishop.review.controller;

import com.jishop.review.dto.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "상품 리뷰 API")
public interface ReviewController {

    ResponseEntity<MyPageDetailReviewResponse> getMyPageDetailReview(Long reviewId, Long userId);

    ResponseEntity<String> likeReview(Long reviewId, LikerIdRequest userIdRequest);

    ResponseEntity<String> unlikeReview(Long reviewId, LikerIdRequest likerIdRequest);

    ResponseEntity<Long> craeteReview(ReviewRequest reviewRequest);

    ResponseEntity<PagedModel<MyPageReviewResponse>> getMyPageReview(Pageable pageable);

    ResponseEntity<PagedModel<?>> getProductReview(Long productId, Long userid, Pageable pageable);

    ResponseEntity<String> updateReview(Long reviewId, Long userId, UpdateReviewRequest updateReviewRequest);

    ResponseEntity<String> deleteReview(Long reviewId, Long userId);
}
