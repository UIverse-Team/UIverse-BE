package com.jishop.wishlist.service;

import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.member.domain.User;
import com.jishop.product.domain.Product;
import com.jishop.product.repository.ProductRepository;
import com.jishop.wishlist.domain.WishList;
import com.jishop.wishlist.dto.WishProductRequest;
import com.jishop.wishlist.dto.WishProductResponse;
import com.jishop.wishlist.repository.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService {

    private final ProductRepository productRepository;
    private final WishListRepository wishListRepository;

    @Override
    public void addProduct(User user, WishProductRequest request) {
        // 상품 들고오기
        Product product = productRepository.findById(request.productId())
                .orElseThrow(()->new DomainException(ErrorType.PRODUCT_NOT_FOUND));
        // 해당 상품 id 들고오기
        Optional<WishList> wishproduct = wishListRepository.findByUserAndProduct(user, product);
        // 해당 상품 id가 wishlist에 존재한다면? 해당 위시리스트 지우기
        if(wishproduct.isPresent()) {
            wishListRepository.delete(wishproduct.get());
        }else{ // else문 꼭 필요함
            WishList wishList = WishList.builder()
                    .user(user)
                    .product(product)
                    .build();
            wishListRepository.save(wishList);
        }
    }

    @Override
    public List<WishProductResponse> getWishProducts() {
        // 전체 찾아오기
        List<WishList> wishList = wishListRepository.findAll();
        // 위시리스트에서 상품 id가져와서? 해당 아이디들로 해당 상품 내용으로 변환하기
        List<Long> productIds = wishList.stream().map(wish -> wish.getProduct().getId()).toList();
        List<Product> products = productRepository.findAllById(productIds);

        return products.stream().map(product -> new WishProductResponse(product))
                .toList();
    }
}
