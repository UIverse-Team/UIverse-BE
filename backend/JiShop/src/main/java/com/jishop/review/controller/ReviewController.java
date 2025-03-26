package com.jishop.review.controller;

import com.jishop.common.response.SliceResponse;
import com.jishop.member.annotation.CurrentUser;
import com.jishop.member.domain.User;
import com.jishop.review.dto.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "상품 리뷰 API")
public interface ReviewController {

    ResponseEntity<PagedModel<ReviewWriteResponse>> getMyPageReviewWrite(User user, Pageable pageable);

    ResponseEntity<SliceResponse<ReviewImageResponse>> getReviewImages(Pageable pageable);


    ResponseEntity<?> getDetailReview(Long reviewId, User user);

    ResponseEntity<String> likeReview(Long reviewId, LikerIdRequest userIdRequest);

    ResponseEntity<String> unlikeReview(Long reviewId, LikerIdRequest likerIdRequest);

    ResponseEntity<Long> createReview(User user, ReviewRequest reviewRequest);

    ResponseEntity<PagedModel<MyPageReviewResponse>> getMyPageReview(User user, Pageable pageable);

    ResponseEntity<PagedModel<?>> getProductReview(User user, Long productId, Pageable pageable);

    ResponseEntity<String> updateReview(Long reviewId, Long userId, UpdateReviewRequest updateReviewRequest);

    ResponseEntity<String> deleteReview(Long reviewId, Long userId);
}
