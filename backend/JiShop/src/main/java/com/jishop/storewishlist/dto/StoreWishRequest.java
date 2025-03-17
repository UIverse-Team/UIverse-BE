package com.jishop.storewishlist.dto;

import com.jishop.member.domain.User;
import com.jishop.store.domain.Store;
import com.jishop.storewishlist.domain.StoreWishList;

public record StoreWishRequest(
        Long storeId
) {
    public StoreWishList toEntity(User user, Store store){
        return StoreWishList.builder()
                .user(user)
                .store(store)
                .build();
    }
}
