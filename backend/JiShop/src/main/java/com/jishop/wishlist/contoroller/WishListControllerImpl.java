package com.jishop.wishlist.contoroller;

import com.jishop.member.annotation.CurrentUser;
import com.jishop.member.domain.User;
import com.jishop.wishlist.dto.WishProductRequest;
import com.jishop.wishlist.dto.WishProductResponse;
import com.jishop.wishlist.service.WishListService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "찜 API")
@RequiredArgsConstructor
@RequestMapping("/wishlist")
public class WishListControllerImpl implements WishListController {

    private final WishListService wishListService;

    @PostMapping("/addProduct")
    public ResponseEntity<String> addWishProduct(@CurrentUser User user,
                                                 @RequestBody WishProductRequest request){
        wishListService.addProduct(user, request);

        return ResponseEntity.ok("success");
    }

    @GetMapping("/getWishList")
    public List<WishProductResponse> getWishProducts() {
        return wishListService.getWishProducts();
    }
}
