package com.jishop.productwishlist.service;

import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.member.domain.User;
import com.jishop.product.domain.Product;
import com.jishop.product.repository.ProductRepository;
import com.jishop.productwishlist.domain.ProductWishList;
import com.jishop.productwishlist.dto.ProductWishProductRequest;
import com.jishop.productwishlist.dto.ProductWishProductResponse;
import com.jishop.productwishlist.repository.ProductWishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductWishListServiceImpl implements ProductWishListService {

    private final ProductRepository productRepository;
    private final ProductWishListRepository wishListRepository;

    @Override
    public void addProduct(User user, ProductWishProductRequest request) {
        // 상품 들고오기
        Product product = productRepository.findById(request.productId())
                .orElseThrow(()->new DomainException(ErrorType.PRODUCT_NOT_FOUND));
        // 해당 상품 id 들고오기
        Optional<ProductWishList> wishproduct = wishListRepository.findByUserAndProduct(user, product);
        // 해당 상품 id가 wishlist에 존재한다면? 해당 위시리스트 지우기
        if(wishproduct.isPresent()) {
            wishListRepository.delete(wishproduct.get());
        }else{ // else문 꼭 필요함
            wishListRepository.save(request.toEntity(user, product));
        }
    }

    @Override
    public List<ProductWishProductResponse> getWishProducts(User user) {
        // 전체 찾아오기
        List<ProductWishList> wishList = wishListRepository.findAllByUser(user);
        // 위시리스트에서 상품 id가져와서? 해당 아이디들로 해당 상품 내용으로 변환하기
        List<Long> productIds = wishList.stream().map(wish -> wish.getProduct().getId()).toList();
        List<Product> products = productRepository.findAllById(productIds);

        return products.stream().map(ProductWishProductResponse::new).toList();
    }
}
