package com.jishop.review;

import com.jishop.review.domain.Review;
import com.jishop.review.repository.ReviewRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class reviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    @DisplayName("패치조인 잘 가져오나. 확인")
    void findByProductIdWithUser() throws Exception {

        // given
        PageRequest pageable = getCreatedAt();
        Page<Review> reviews = reviewRepository.findByProductIdWithUser(2L,pageable);

        reviews.stream().forEach((review) -> {
            System.out.println(review.getUser().getName());
        });
        //when

        //then

    }
    private PageRequest getCreatedAt() {
        return PageRequest.of(0, 15, Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    @Test
    @DisplayName("레포 테스트")
    void MyPageReview() throws Exception {

        // given
        PageRequest createdAt = getCreatedAt();
        Page<Review> reviewsProductByUserId = reviewRepository.findReviewsProductByUserId(1L, createdAt);

        //when
        reviewsProductByUserId.stream().forEach((review) -> {
            System.out.println(review.getProduct().getName());
        });

        //then
    }

    @Test
    @DisplayName("상품 리뷰 가져오기")
    void getReviewProduct() throws Exception {
        // given
        Review review = reviewRepository.findByReviewIdAndUserId(1L, 5L).orElseThrow(
                () -> new IllegalStateException("sdfkj")
        );
        //when
        System.out.println(review.getProductSummary());
        //then

    }
}
