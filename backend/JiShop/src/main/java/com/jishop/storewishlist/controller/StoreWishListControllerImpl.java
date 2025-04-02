package com.jishop.storewishlist.controller;

import com.jishop.member.annotation.CurrentUser;
import com.jishop.member.domain.User;
import com.jishop.storewishlist.dto.StoreWishListResponse;
import com.jishop.storewishlist.dto.StoreWishRequest;
import com.jishop.storewishlist.service.StoreWishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/storewishlist")
public class StoreWishListControllerImpl implements StoreWIshListController{

    private final StoreWishListService storeWishListService;

    @Override
    @PostMapping("/add")
    public ResponseEntity<String> addWishStore(@CurrentUser User user,
                                               @RequestBody StoreWishRequest request) {
        storeWishListService.addWishStore(user, request);

        return ResponseEntity.ok("스토어 찜 완료~!");
    }

    @Override
    @GetMapping("/getList")
    public List<StoreWishListResponse> getWishStores(@CurrentUser User user) {
        return storeWishListService.getWishStores(user);
    }
}
