package com.jishop.review.service;

import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.domain.User;
import com.jishop.order.domain.OrderDetail;
import com.jishop.order.repository.OrderDetailRepository;
import com.jishop.repository.UserRepository;
import com.jishop.review.domain.Review;
import com.jishop.review.dto.ReviewRequest;
import com.jishop.review.dto.ReviewResponse;
import com.jishop.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Override
    public Long createReview(ReviewRequest reviewRequest, List<String> images, Long userId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new DomainException(ErrorType.USER_NOT_FOUND)
        );

        //todo:
        // 1. 주문 상세, 옵션, 상품 오고 리뷰 상품 이름 만들기. -> 만들어서 넣을 필요가 있을까??
        // 2. 상품 테이블에서 리뷰개수와 리뷰평점 업데이트 하기. -> 트랜잭션 락 걸어야지.
        // 3. review 생성.

        OrderDetail orderDetail = orderDetailRepository.findOrderDetailForReviewById(reviewRequest.orderDetailId())
                .orElseThrow(() -> new DomainException(ErrorType.ORDER_DETAIL_NOT_FOUND));

        String productSummary = "상품명 + ,옵션 + ,수량";

        Review review = reviewRepository.save(reviewRequest.toEntity(images, user, productSummary));

        return review.getId();
    }

    @Override
    public PagedModel<ReviewResponse> getProductReviews(Long saleProductId, Pageable pageable) {
        //todo:
        // 1. 가져올거 -> 별점, 사진, 옵션, tag 값, 대표 상품 값
        // 2. 주문 상세, 판매 상품 saleProductId로 조인 List<order_detail> a 받아옴
        // 3. a




        return null;
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
