package com.jishop.review.service;

import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.member.domain.User;
import com.jishop.member.repository.UserRepository;
import com.jishop.order.domain.OrderDetail;
import com.jishop.order.repository.OrderDetailRepository;
import com.jishop.product.domain.Product;
import com.jishop.review.domain.LikeReview;
import com.jishop.review.domain.Review;
import com.jishop.review.dto.*;
import com.jishop.review.repository.LikeReviewRepository;
import com.jishop.review.repository.ReviewRepository;
import com.jishop.reviewproduct.domain.ReviewProduct;
import com.jishop.reviewproduct.repository.ReviewProductRepository;
import com.jishop.saleproduct.domain.SaleProduct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final LikeReviewRepository likeReviewRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ReviewProductRepository reviewProductRepository;

    @Override
    public Long createReview(ReviewRequest reviewRequest, List<String> images, Long userId) {

        // 리뷰 중복 방지
        boolean isDuplicate = reviewRepository.existsByOrderDetailId(reviewRequest.orderDetailId());
        if (isDuplicate) {
            throw new DomainException(ErrorType.REVIEW_DUPLICATE);
        }
        User user = userRepository.findById(userId).orElseThrow(
                () -> new DomainException(ErrorType.USER_NOT_FOUND)
        );

        //todo:
        // 2. 상품 테이블에서 리뷰개수와 리뷰평점 업데이트 하기. -> 트랜잭션 락 걸어야지.
        // 3. 카테고리별 상품 리뷰 달기 고려?

        OrderDetail orderDetail = orderDetailRepository.findOrderDetailForReviewById(reviewRequest.orderDetailId())
                .orElseThrow(() -> new DomainException(ErrorType.ORDER_DETAIL_NOT_FOUND));

        SaleProduct saleProduct = orderDetail.getSaleProduct();

        String productSummary = null;

        if (saleProduct.getOption() == null) {
            productSummary = String.format("%s;%s",
                    saleProduct.getName(),
                    orderDetail.getQuantity());
        } else {
            productSummary = String.format("%s;%s;%s",
                    saleProduct.getName(),
                    saleProduct.getOption().getOptionValue(),
                    orderDetail.getQuantity());
        }

        Product product = saleProduct.getProduct();

        // 동시성 고려 x
        ReviewProduct reviewProduct = reviewProductRepository.findByProduct(product)
                .orElseGet(() -> {
                    var newReviewProduct = ReviewProduct.builder()
                            .reviewCount(0)
                            .reviewScore(0)
                            .product(product)
                            .build();
                    return reviewProductRepository.save(newReviewProduct);
                });

        reviewProduct.increaseRating(reviewRequest.rating());

        // 리뷰 저장
        Review review = reviewRepository.save(reviewRequest.toEntity(images, product, orderDetail, user, productSummary));

        return review.getId();
    }

    @Override
    public PagedModel<ReviewWithOutUserResponse> getProductReviewsWithoutUser(Long productId, Pageable pageable) {
        //todo:
        // 1. 가져올거 -> 별점, 리뷰한 날짜, 사진, 옵션, tag 값, 대표 상품 값, 회원 이름?
        // 2. 주문 상세, 판매 상품 productId으로 review 확인 조인 List<order_detail> a 받아옴
        // 3. 기본 내림차순
        // 4. 좋아요 개수랑 해당 유저가 좋아요를 할 수 있는지 둬 줘야 한다.
        // 5. 로그인 안했을때.

        return new PagedModel<>(reviewRepository
                .findByProductIdWithUser(productId, pageable)
                .map(ReviewWithOutUserResponse::from));
    }

    @Override
    public PagedModel<ReviewWithUserResponse> getProductReviewsWithUser(Long productId, Long userId, Pageable pageable) {
        //todo:
        // 1. 로그인 했을때.
        return new PagedModel<>(reviewRepository
                .findReviewsWithUserLike(productId, userId, pageable)
                .map(result -> {
                            Review r = (Review) result[0];
                            Boolean isLike = (Boolean) result[1];
                            return ReviewWithUserResponse.from(r, isLike);
                        }
                ));
    }

    @Override
    public PagedModel<MyPageReviewResponse> getMyPageReviews(Long userId, Pageable pageable) {
        return new PagedModel<>(reviewRepository
                .findReviewsProductByUserId(userId, pageable)
                .map(MyPageReviewResponse::from));
    }

    @Override
    public void likeReview(LikerIdRequest likerIdRequest, Long reviewId) {

        //todo:
        // 1. 쿼리 개선
        // 2. 동시성 고려
        // 3. db 에러 잡아서 커스텀하기. 500 에러를 반환하여서.?

        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new DomainException(ErrorType.REVIEW_NOT_FOUND)
        );

        User user = userRepository.findById(likerIdRequest.likerId()).orElseThrow(
                () -> new DomainException(ErrorType.USER_NOT_FOUND)
        );

        LikeReview likeReview = LikeReview.builder()
                .user(user)
                .review(review)
                .build();

        likeReviewRepository.save(likeReview);
        review.increaseLikeCount();
    }

    @Override
    public void unlikeReview(LikerIdRequest likerIdRequest, Long reviewId) {
        //todo:
        // 1. 쿼리 개선
        // 2. 동시성 락 걸기 개선 해야함..

        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new DomainException(ErrorType.REVIEW_NOT_FOUND)
        );

        User user = userRepository.findById(likerIdRequest.likerId()).orElseThrow(
                () -> new DomainException(ErrorType.USER_NOT_FOUND)
        );

        /**
         * 이미 없는 값을 삭제하려고 한다면 에러를 터트려야할까??
         */
        int flag = likeReviewRepository.deleteByReviewAndUser(review, user);
        if (flag == 1) {
            review.decreaseLikeCount();
        }
    }

    @Override
    public void updateReview(Long reviewId, Long userId, UpdateReviewRequest updateReviewRequest) {

        Review review = reviewRepository.findByReviewIdAndUserId(reviewId, userId).orElseThrow(
                () -> new DomainException(ErrorType.MATCH_NOT_USER)
        );

        review.updateReview(updateReviewRequest);
    }

    @Override
    public void deleteReview(Long reviewId, Long productId, Long userId) {
        //todo:
        // 1. 이미 삭제한 리뷰인지 확인.
        // 2. 상품 리뷰가 상품이 삭제 되었는지 확인해야 되나?
        // 3. 리뷰 삭제 동시성 처리 어떻게 해주냐?? -> 단순하게 update해준다.
        // 4. 동시성 문제 - 같은 유저가 리뷰에 동시 2번 이상 눌렀을때 생각..
        Review review = reviewRepository.findByReviewIdAndUserId(reviewId, userId).orElseThrow(
                () -> new DomainException(ErrorType.MATCH_NOT_USER)
        );

        if(review.isDeleteStatus()){
            throw new DomainException(ErrorType.DATA_ALREADY_DELETED);
        }

        reviewProductRepository.decreaseRatingAtomic(productId, review.getRating());
        review.delete();
    }
}
