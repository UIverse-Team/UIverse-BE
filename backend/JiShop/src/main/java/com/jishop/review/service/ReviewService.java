package com.jishop.review.service;

import com.jishop.review.dto.ReviewRequest;
import com.jishop.review.dto.ReviewResponse;

import java.util.List;

public interface ReviewService {
    Long createReview(ReviewRequest reviewRequest, List<String> images, Long userId);
    List<ReviewResponse> getProductReviews(Long productId);
    List<ReviewResponse> getUserReviews(Long userId);
    void updateReview(Long reviewId, ReviewRequest reviewRequest);
    void deleteReview(Long reviewId);
}
