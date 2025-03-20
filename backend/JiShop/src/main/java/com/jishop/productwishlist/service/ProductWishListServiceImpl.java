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
import com.jishop.store.domain.Store;
import com.jishop.storewishlist.domain.StoreWishList;
import com.jishop.storewishlist.dto.StoreWishRequest;
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
        Product product = productRepository.findById(request.productId())
                .orElseThrow(()->new DomainException(ErrorType.PRODUCT_NOT_FOUND));

        Optional<ProductWishList> wishproduct = wishListRepository.findByUserAndProduct(user, product);

        if(wishproduct.isEmpty()) {
            ProductWishList newWish = request.toEntity(user, product);
            newWish.onStatus();
            wishListRepository.save(newWish);
            product.incrementWishCount();
        }else{ // else문 꼭 필요함
            ProductWishList existingWish = wishproduct.get();
            existingWish.offStatus();
            wishListRepository.delete(existingWish);
            product.decrementWishCount();

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
