package com.jishop.review.service;

import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.member.domain.User;
import com.jishop.member.repository.UserRepository;
import com.jishop.order.domain.OrderDetail;
import com.jishop.order.repository.OrderDetailRepository;
import com.jishop.product.domain.Product;
import com.jishop.review.dto.ReviewWriteResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
    public Long createReview(ReviewRequest reviewRequest, User user) {

        OrderDetail orderDetail = orderDetailRepository.findOrderDetailForReviewById(reviewRequest.orderDetailId())
                .orElseThrow(() -> new DomainException(ErrorType.ORDER_DETAIL_NOT_FOUND));

        SaleProduct saleProduct = orderDetail.getSaleProduct();

        String productSummary = makeProductSummar(saleProduct, orderDetail);

        Product product = saleProduct.getProduct();

        // 같은 상품을 동시에 리뷰를 작성한다면 상품 리뷰 카운트와 스코어를 올려주어야 한다. 비관적락 사용
        ReviewProduct reviewProduct = reviewProductRepository.findByProductForUpdate(product)
                .orElseGet(() -> {
                    var newReviewProduct = ReviewProduct.builder()
                            .reviewCount(0)
                            .reviewScore(0)
                            .product(product)
                            .build();
                    return reviewProductRepository.saveAndFlush(newReviewProduct);
                });

        reviewProduct.increaseRating(reviewRequest.rating());

        // 리뷰 저장
        Review review = reviewRepository.save(reviewRequest.toEntity(reviewRequest.images(), product, orderDetail, user, productSummary));

        return review.getId();
    }

    private String makeProductSummar(SaleProduct saleProduct, OrderDetail orderDetail) {
        if (saleProduct.getOption() == null) {
            return String.format("%s;%s",
                    saleProduct.getName(),
                    orderDetail.getQuantity());
        }

        return String.format("%s;%s;%s",
                saleProduct.getName(),
                saleProduct.getOption().getOptionValue(),
                orderDetail.getQuantity());
    }

    @Override
    public PagedModel<ReviewWithOutUserResponse> getProductReviews(Long productId, Pageable pageable) {

        return new PagedModel<>(reviewRepository
                .findByProductIdWithUser(productId, pageable)
                .map(ReviewWithOutUserResponse::from));
    }

    @Override
    public PagedModel<ReviewWithUserResponse> getProductReviews(Long productId, Long userId, Pageable pageable) {
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
    public ReviewWithOutUserResponse getDetailReview(Long reviewId) {

        return reviewRepository.findByReviewIdWithUser(reviewId)
                .map(ReviewWithOutUserResponse::from)
                .orElseThrow(() -> new DomainException(ErrorType.REVIEW_NOT_FOUND));
    }

    @Override
    public ReviewWithUserResponse getDetailReview(Long reviewId, User user) {

        return reviewRepository.findReviewsWithUserLike(reviewId, user.getId())
                .map(object -> {
                    Object[] result = (Object[]) object;
                    Review r = (Review) result[0];
                    Boolean isLike = (Boolean) result[1];
                    return ReviewWithUserResponse.from(r, isLike);
                }).orElseThrow(() -> new DomainException(ErrorType.REVIEW_NOT_FOUND));
    }

    @Override
    public PagedModel<ReviewWriteResponse> getMyPageReviewWrite(User user, Pageable pageable) {

        return new PagedModel<>(reviewRepository
                .findByMyPageReviewWrite(user.getId(), pageable));
    }

    @Override
    public Slice<ReviewImageResponse> getReviewImages(Pageable pageable) {
        Slice<ReviewImageResponse> reviews = reviewRepository.findByAllWithImage(pageable)
                .map(ReviewImageResponse::from);
        return reviews;
    }


    @Override
    public void likeReview(LikerIdRequest likerIdRequest, Long reviewId) {

        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new DomainException(ErrorType.REVIEW_NOT_FOUND)
        );

        User liker = userRepository.findById(likerIdRequest.likerId()).orElseThrow(
                () -> new DomainException(ErrorType.USER_NOT_FOUND)
        );

        LikeReview likeReview = LikeReview.builder()
                .user(liker)
                .review(review)
                .build();

        likeReviewRepository.saveAndFlush(likeReview);
        review.increaseLikeCount();
    }

    @Override
    public void unlikeReview(LikerIdRequest likerIdRequest, Long reviewId) {

        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new DomainException(ErrorType.REVIEW_NOT_FOUND)
        );

        User unliker = userRepository.findById(likerIdRequest.likerId()).orElseThrow(
                () -> new DomainException(ErrorType.USER_NOT_FOUND)
        );

        int flag = likeReviewRepository.deleteByReviewAndUser(review, unliker);
        if (flag == 1) review.decreaseLikeCount();
    }

    @Override
    public void updateReview(Long reviewId, Long userId, UpdateReviewRequest updateReviewRequest) {

        Review review = reviewRepository.findByReviewIdAndUserId(reviewId, userId).orElseThrow(
                () -> new DomainException(ErrorType.MATCH_NOT_USER)
        );

        review.updateReview(updateReviewRequest);
    }

    @Override
    public void deleteReview(Long reviewId, Long userId) {

        Review review = reviewRepository.findByIdWithLock(reviewId).orElseThrow(
                () -> new DomainException(ErrorType.REVIEW_NOT_FOUND)
        );

        if (!review.getUser().getId().equals(userId)) throw new DomainException(ErrorType.MATCH_NOT_USER);

        if (review.isDeleteStatus()) throw new DomainException(ErrorType.DATA_ALREADY_DELETED);

        Product product = review.getProduct();

        reviewProductRepository.decreaseRatingAtomic(product, review.getRating());
        review.delete(); // 리뷰는 소프트 딜리트로
        reviewRepository.save(review); // 빠른 커밋을 위해
    }
}
