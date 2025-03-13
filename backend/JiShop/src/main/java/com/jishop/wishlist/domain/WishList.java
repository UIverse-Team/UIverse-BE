package com.jishop.wishlist.domain;

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
public class WishList extends BaseEntity {

    /* 유저 id
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;*/

    // 상품 id
    @JoinColumn(name = "product_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Builder
    public WishList(/*User user,*/ Product product) {
        //this.user = user;
        this.product = product;
    }
}
