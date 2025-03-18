package com.jishop.storewishlist.controller;

import com.jishop.member.domain.User;
import com.jishop.storewishlist.dto.StoreWishListResponse;
import com.jishop.storewishlist.dto.StoreWishRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "스토어 찜 API")
public interface StoreWIshListController {

    ResponseEntity<String> addWishStore(User user, StoreWishRequest request);
    List<StoreWishListResponse> getWishStores(User user);
}
