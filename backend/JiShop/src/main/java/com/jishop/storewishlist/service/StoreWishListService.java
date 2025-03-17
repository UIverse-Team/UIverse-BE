package com.jishop.storewishlist.service;

import com.jishop.member.domain.User;
import com.jishop.storewishlist.dto.StoreWishListResponse;
import com.jishop.storewishlist.dto.StoreWishRequest;

import java.util.List;

public interface StoreWishListService {

    void addWishStore(User user, StoreWishRequest request);
    List<StoreWishListResponse> getWishStores(User user);
}
