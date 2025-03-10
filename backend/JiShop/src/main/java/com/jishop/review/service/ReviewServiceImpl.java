package com.jishop.review.service;

import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.domain.User;
import com.jishop.repository.UserRepository;
import com.jishop.review.domain.Review;
import com.jishop.review.dto.ReviewRequest;
import com.jishop.review.dto.ReviewResponse;
import com.jishop.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Override
    public Long createReview(ReviewRequest reviewRequest, List<String> images, Long userId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new DomainException(ErrorType.USER_NOT_FOUND)
        );

        Review review = reviewRepository.save(reviewRequest.toEntity(images, user));

        return review.getId();
    }

    @Override
    public List<ReviewResponse> getProductReviews(Long productId) {
        return List.of();
    }

    @Override
    public List<ReviewResponse> getUserReviews(Long userId) {
        return List.of();
    }

    @Override
    public void updateReview(Long reviewId, ReviewRequest reviewRequest) {

    }

    @Override
    public void deleteReview(Long reviewId) {

    }

}
