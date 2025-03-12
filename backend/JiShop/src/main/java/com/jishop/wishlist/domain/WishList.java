package com.jishop.wishlist.domain;

import com.jishop.common.util.BaseEntity;
import com.jishop.member.domain.User;
import com.jishop.product.domain.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WishList extends BaseEntity {

    /*// 유저 id
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;*/

    // 상품 id
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Builder
    public WishList(/*User user,*/ Product product) {
        //this.user = user;
        this.product = product;
    }
}
