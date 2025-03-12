package com.jishop.wishlist.domain;

import com.jishop.common.util.BaseEntity;
import com.jishop.member.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class WishList extends BaseEntity {

    @ManyToOne()
    private User user;

    // 상품 id

}
