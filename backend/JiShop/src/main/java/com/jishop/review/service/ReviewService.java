package com.jishop.review.service;

import com.jishop.review.dto.MyPageReviewResponse;
import com.jishop.review.dto.ReviewRequest;
import com.jishop.review.dto.ReviewResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

import java.util.List;

public interface ReviewService {

    void deleteReview(Long reviewId);
    void updateReview(Long reviewId, ReviewRequest reviewRequest);
    PagedModel<ReviewResponse> getProductReviews(Long productId, Pageable pageable);
    Long createReview(ReviewRequest reviewRequest, List<String> images, Long userId);
    PagedModel<MyPageReviewResponse> getMyPageReviews(Long userId, Pageable pageable);
}
