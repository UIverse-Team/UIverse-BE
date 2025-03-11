package com.jishop.review.domain;

import com.jishop.common.util.BaseEntity;
import com.jishop.domain.User;
import com.jishop.order.domain.Order;
import com.jishop.order.domain.OrderDetail;
import com.jishop.review.domain.embed.ImageUrls;
import com.jishop.review.domain.tag.Tag;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import java.util.List;

@Entity
@Getter
@Table(name = "reviews")
@Check(constraints = "rating BETWEEN 1 AND 5")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_detail_id")
    private OrderDetail orderDetail;

    @Column(nullable = false)
    private String productSummary;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int rating;

    @Enumerated(EnumType.STRING)
    private Tag tag;

    @Embedded
    private ImageUrls imageUrls;

    @Builder
    public Review(Order order, OrderDetail orderDetail ,User user,
                  String productSummary, String content, int rating,
                  Tag tag, List<String> imageUrls) {
        this.tag = tag;
        this.user = user;
        this.rating = rating;
        this.order = order;
        this.content = content;
        this.orderDetail = orderDetail;
        this.productSummary = productSummary;
        this.imageUrls = new ImageUrls(imageUrls);
    }
}
