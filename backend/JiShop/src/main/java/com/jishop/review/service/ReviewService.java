package com.jishop.review.service;

import com.jishop.member.domain.User;
import com.jishop.review.dto.ReviewWriteResponse;
import com.jishop.review.dto.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PagedModel;

public interface ReviewService {

    Long createReview(ReviewRequest reviewRequest, User user);


    PagedModel<MyPageReviewResponse> getMyPageReviews(Long userId, Pageable pageable);

    PagedModel<ReviewWithOutUserResponse> getProductReviews(Long productId, Pageable pageable);
    PagedModel<ReviewWithUserResponse> getProductReviews(Long productId, Long userId, Pageable pageable);

    void likeReview(LikerIdRequest userIdRequest, Long ReviewId);

    void unlikeReview(LikerIdRequest userIdRequest, Long ReviewId);

    void updateReview(Long reviewId, Long userId, UpdateReviewRequest updateReviewRequest);

    void deleteReview(Long reviewId, Long userId);

    PagedModel<ReviewWriteResponse> getMyPageReviewWrite(User user, Pageable pageable);

    Slice<ReviewImageResponse> getReviewImages(Pageable pageable);

    ReviewWithOutUserResponse getDetailReview(Long reviewId);
    ReviewWithUserResponse getDetailReview(Long reviewId, User user);
}
