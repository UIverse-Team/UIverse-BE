package com.jishop.review;

import com.jishop.order.domain.OrderDetail;
import com.jishop.order.repository.OrderRepository;
import com.jishop.review.repository.ReviewRepository;
import com.jishop.review.service.ReviewService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@ExtendWith(MockitoExtension.class)
public class ReviewTest {

    @MockitoBean
    private OrderRepository orderRepository;
    @MockitoBean
    private ReviewRepository reviewRepository;
    @InjectMocks
    private ReviewService reviewService;

    @Test
    @DisplayName("create 테스트")
    void craete() throws Exception {
        // given


        //when

        //then

    }

    private OrderDetail createOrderDetail() {
        return OrderDetail.builder()
                .build();
    }

}
