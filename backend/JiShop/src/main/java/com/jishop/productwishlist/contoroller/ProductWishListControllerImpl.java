package com.jishop.productwishlist.contoroller;

import com.jishop.member.annotation.CurrentUser;
import com.jishop.member.domain.User;
import com.jishop.productwishlist.dto.ProductWishProductRequest;
import com.jishop.productwishlist.dto.ProductWishProductResponse;
import com.jishop.productwishlist.service.ProductWishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/productwishlist")
public class ProductWishListControllerImpl implements ProductWishListController {

    private final ProductWishListService wishListService;

    @PostMapping("/add")
    public ResponseEntity<String> addWishProduct(@CurrentUser User user,
                                                 @RequestBody ProductWishProductRequest request){
        wishListService.addProduct(user, request);

        return ResponseEntity.ok("찜 추가 완료~");
    }

    @GetMapping("/getList")
    public List<ProductWishProductResponse> getWishProducts(@CurrentUser User user) {
        return wishListService.getWishProducts(user);
    }
}
