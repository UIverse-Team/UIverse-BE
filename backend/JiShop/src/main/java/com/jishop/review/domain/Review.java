package com.jishop.review.domain;

import com.jishop.common.util.BaseEntity;
import com.jishop.domain.User;
import com.jishop.review.domain.embed.ImageUrls;
import com.jishop.review.domain.tag.Tag;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Table(name = "reviews")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private Long productId;

    private Long orderDetailId;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Integer rate;

    @Enumerated(EnumType.STRING)
    private Tag tag;

    @Embedded
    private ImageUrls imageUrls;

    @Builder
    public Review(Long productId, User user, String content, Integer rate, Tag tag, List<String> imageUrls) {
        this.productId = productId;
        this.user = user;
        this.content = content;
        this.rate = rate;
        this.tag = tag;
        this.imageUrls = new ImageUrls(imageUrls);
    }
}
