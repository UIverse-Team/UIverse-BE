package com.jishop.productwishlist.domain;

import com.jishop.common.util.BaseEntity;
import com.jishop.member.domain.User;
import com.jishop.product.domain.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductWishList extends BaseEntity {

    // 유저 id
    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // 상품 id
    @JoinColumn(name = "product_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    private boolean productWishStatus;

    @Version
    private Long version; // 낙관적 락을 위한 필드

    @Builder
    public ProductWishList(User user, Product product) {
        this.user = user;
        this.product = product;
        this.productWishStatus = false;
    }

    public void onStatus(){
        this.productWishStatus = true;
    }

    public void offStatus(){
        this.productWishStatus = false;
    }
}
