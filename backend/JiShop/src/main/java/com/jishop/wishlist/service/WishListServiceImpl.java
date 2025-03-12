package com.jishop.wishlist.service;

import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.product.domain.Product;
import com.jishop.product.repository.ProductRepository;
import com.jishop.wishlist.domain.WishList;
import com.jishop.wishlist.dto.WishProductRequest;
import com.jishop.wishlist.dto.WishProductResponse;
import com.jishop.wishlist.repository.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService {

    private final ProductRepository productRepository;
    private final WishListRepository wishListRepository;


    /**
     * 추후 동일 상품을 한번더 add 한다면 찜 취소되게 만들어야함
     * @param request
     */
    @Override
    public void addProduct(WishProductRequest request) {
        Product product = productRepository.findById(request.productId())
                .orElseThrow(()->new DomainException(ErrorType.PRODUCT_NOT_FOUND));

        WishList wishList = WishList.builder()
                .product(product)
                .build();

        wishListRepository.save(wishList);
    }

    @Override
    public List<WishProductResponse> getWishProducts() {
        // 전체 찾아오기
        List<WishList> wishList = wishListRepository.findAll();

        // 위시리스트에서 상품 id가져와서? 해당 아이디들로 해당 상품 내용으로 변환하기
        List<Long> productIds = wishList.stream().map(WishList::getId).collect(Collectors.toList());
        List<Product> products = productRepository.findAllById(productIds);

        return products.stream().map(product -> new WishProductResponse(product))
                .collect(Collectors.toList());
    }
}
