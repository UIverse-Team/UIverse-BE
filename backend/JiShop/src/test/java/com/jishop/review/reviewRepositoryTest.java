package com.jishop.review;

import com.jishop.review.domain.Review;
import com.jishop.review.dto.MyPageReviewResponse;
import com.jishop.review.dto.ReviewImageResponse;
import com.jishop.review.repository.ReviewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class reviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    @DisplayName("리뷰 이미지 슬라이스")
    void imagesSlice() throws Exception {
        // given
        PageRequest pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "createdAt"));
        Slice<ReviewImageResponse> reviewSlice= reviewRepository.findByAllWithImage(pageable).map(ReviewImageResponse::from);
        //then
        System.out.println(reviewSlice.getContent());
        System.out.println(reviewSlice.getNumber());
        System.out.println(reviewSlice.hasNext());

    }
}
