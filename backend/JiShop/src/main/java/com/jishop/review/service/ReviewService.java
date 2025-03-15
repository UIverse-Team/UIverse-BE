package com.jishop.review.service;

import com.jishop.review.dto.MyPageReviewResponse;
import com.jishop.review.dto.ReviewRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

import java.util.List;

public interface ReviewService {

    Long createReview(ReviewRequest reviewRequest, List<String> images, Long userId);


    PagedModel<?> getProductReviewsWithoutUser(Long productId, Pageable pageable);
    PagedModel<MyPageReviewResponse> getMyPageReviews(Long userId, Pageable pageable);
    PagedModel<?> getProductReviewsWithUser(Long productId, Long userId, Pageable pageable);

    void likeReview(Long reviewId, Long userId);

    void deleteReview(Long reviewId);

    void updateReview(Long reviewId, ReviewRequest reviewRequest);


}
