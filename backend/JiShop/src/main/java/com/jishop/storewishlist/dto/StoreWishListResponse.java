package com.jishop.storewishlist.dto;

import com.jishop.store.domain.Store;

public record StoreWishListResponse(
        Long storeId,
        String mallName
) {
    public StoreWishListResponse(Store store){
        this(store.getId(), store.getMallName());
    }
}
