package com.jishop.review.domain;

import com.jishop.common.util.BaseEntity;
import com.jishop.member.domain.User;
import com.jishop.order.domain.OrderDetail;
import com.jishop.product.domain.Product;
import com.jishop.review.domain.embed.ImageUrls;
import com.jishop.review.domain.tag.Tag;
import com.jishop.review.dto.UpdateReviewRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;
import java.util.Optional;

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
    @JoinColumn(name = "order_detail_id", unique = true, nullable = false)
    private OrderDetail orderDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private String productSummary;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int rating;

    @Enumerated(EnumType.STRING)
    private Tag tag;

    @Embedded
    @Getter(AccessLevel.NONE)
    private ImageUrls imageUrls;

    @ColumnDefault("0")
    @Column(nullable = false)
    private int likeCount;

    @Builder
    public Review(OrderDetail orderDetail, Product product, User user,
                  String productSummary, String content, int rating,
                  Tag tag, List<String> imageUrls) {
        this.tag = tag;
        this.user = user;
        this.rating = rating;
        this.content = content;
        this.product = product;
        this.orderDetail = orderDetail;
        this.productSummary = productSummary;
        this.imageUrls = new ImageUrls(imageUrls);
    }

    public void increaseLikeCount() {
        likeCount++;
    }

    public void decreaseLikeCount() {
       likeCount--;
    }

    public void updateReview(UpdateReviewRequest updateReviewRequest) {
        this.content = updateReviewRequest.content();
        this.rating = updateReviewRequest.rating();
        this.tag = updateReviewRequest.tag();
    }

    public ImageUrls getImageUrls() {
        return Optional.ofNullable(imageUrls).orElse(new ImageUrls());
    }
}
