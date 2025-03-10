package com.jishop.review;

import com.jishop.review.dto.ReviewRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class dtoTest {
    @Test
    @DisplayName("request 테스트")
    void request() throws Exception {
        // given
        ReviewRequest request = new ReviewRequest("좋아요", "1통", 1L, 1, 1, null);
        //when
        System.out.println(request);
        //then

    }
}
