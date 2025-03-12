package com.jishop.wishlist.contoroller;

import com.jishop.wishlist.dto.WishProductRequest;
import com.jishop.wishlist.dto.WishProductResponse;
import com.jishop.wishlist.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wishlist")
public class WishListControllerImpl implements WishListController {

    private final WishListService wishListService;

    @PostMapping("/addProduct")
    public ResponseEntity<String> addWishProduct(@RequestBody WishProductRequest request){
        wishListService.addProduct(request);
        return ResponseEntity.ok("success");
    }

    @GetMapping("/getWishList")
    public List<WishProductResponse> getWishProducts() {
        return wishListService.getWishProducts();
    }

}
