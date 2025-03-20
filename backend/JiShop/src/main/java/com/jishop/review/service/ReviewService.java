package com.jishop.review.service;

import com.jishop.review.dto.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

import java.util.List;

public interface ReviewService {

    Long createReview(ReviewRequest reviewRequest, List<String> images, Long userId);

    PagedModel<ReviewWithOutUserResponse> getProductReviewsWithoutUser(Long productId, Pageable pageable);

    PagedModel<MyPageReviewResponse> getMyPageReviews(Long userId, Pageable pageable);

    PagedModel<ReviewWithUserResponse> getProductReviewsWithUser(Long productId, Long userId, Pageable pageable);

    void likeReview(LikerIdRequest userIdRequest, Long ReviewId);

    void unlikeReview(LikerIdRequest userIdRequest, Long ReviewId);

    void updateReview(Long reviewId, Long userId, UpdateReviewRequest updateReviewRequest);

    void deleteReview(Long reviewId, Long userId);
}
