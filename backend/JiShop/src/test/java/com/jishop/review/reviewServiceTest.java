package com.jishop.review;

import com.jishop.review.domain.tag.Tag;
import com.jishop.review.dto.ReviewRequest;
import com.jishop.review.dto.ReviewResponse;
import com.jishop.review.service.ReviewService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class reviewServiceTest {
    @Autowired
    private ReviewService reviewService;

    @Test
    @DisplayName("리뷰 저장")
    void create() throws Exception {
        // given
        ReviewRequest request1 = getReviewRequest(5L, "This is a test", Tag.NEUTRAL, 5);
        ReviewRequest request2 = getReviewRequest(6L, "새로운 테스트", Tag.RECOMMENDED, 5);
        ReviewRequest request3 = getReviewRequest(7L, "물까 테스트", Tag.RECOMMENDED, 3);

        List<String> images = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            images.add("이미지" + i + ".jpg");
        }
        Long review1 = reviewService.createReview(request1, images, 1L);
        Long review2 = reviewService.createReview(request2, images, 2L);
        Long review3 = reviewService.createReview(request3, images, 3L);
    }

    @Test
    @DisplayName("상품에서 조회")
    void getproduct() throws Exception {
        // given
        Long productId = 2L;

        PageRequest pageable = PageRequest.of(0, 15, Sort.by(Sort.Direction.DESC, "createdAt"));
        PagedModel<ReviewResponse> productReviews = reviewService.getProductReviews(productId, pageable);
        //when

        //then
        productReviews.getContent().forEach(System.out::println);

    }

    private ReviewRequest getReviewRequest(Long orderDetailId, String content, Tag tag, int rating) {
        return new ReviewRequest(orderDetailId, content, tag, rating);
    }
}
