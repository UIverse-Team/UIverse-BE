package com.jishop.storewishlist.domain;

import com.jishop.common.util.BaseEntity;
import com.jishop.member.domain.User;
import com.jishop.store.domain.Store;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreWishList extends BaseEntity {

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "store_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;

    private boolean storeWishStatus;

    @Version
    private Long version;

    @Builder
    public StoreWishList(User user, Store store) {
        this.user = user;
        this.store = store;
        this.storeWishStatus = false;
    }

    public void onStatus(){
        this.storeWishStatus = true;
    }

    public void offStatus(){
        this.storeWishStatus = false;
    }
}
