package com.jishop.review;

import com.jishop.review.domain.tag.Tag;
import com.jishop.review.dto.ReviewRequest;
import com.jishop.review.service.ReviewService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
        Long orderDetailId = 1L;
        String content = "This is a test";
        Long userId = 1L;
        int rating = 5;
        Tag tag = Tag.NEUTRAL;
        ReviewRequest request = new ReviewRequest(orderDetailId, content, tag, rating);

        content = "새로운 테스트";
        ReviewRequest request1 = new ReviewRequest(orderDetailId, content, tag, rating);

        List<String> images = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            images.add("이미지" + i + ".jpg");
        }
        Long review = reviewService.createReview(request, images, userId);
        Long review1 = reviewService.createReview(request, images, userId);

        //when

        //then

    }
}
