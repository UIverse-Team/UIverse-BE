package com.jishop.review.controller;

import com.jishop.review.dto.LikerIdRequest;
import com.jishop.review.dto.MyPageReviewResponse;
import com.jishop.review.dto.ReviewRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;

@Tag(name = "상품 리뷰 API")
public interface ReviewController {

    ResponseEntity<String> likeReview(Long reviewId, LikerIdRequest userIdRequest);
    ResponseEntity<String> unlikeReview(Long reviewId, LikerIdRequest likerIdRequest);

    ResponseEntity<Long> craeteReview(ReviewRequest reviewRequest);

    ResponseEntity<PagedModel<MyPageReviewResponse>> getMyPageReview(Pageable pageable);
    ResponseEntity<PagedModel<?>> getProdcutReview(Long productId, Long userid, Pageable pageable);


}
