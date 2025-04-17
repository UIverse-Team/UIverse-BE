package com.jishop.product.service.impl;

import com.jishop.member.domain.User;
import com.jishop.product.domain.Product;
import com.jishop.product.dto.response.ProductResponse;
import com.jishop.product.service.ProductWishlistService;
import com.jishop.productwishlist.repository.ProductWishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductWishlistServiceImpl implements ProductWishlistService {

    private final ProductWishListRepository productWishListRepository;

    @Override
    public List<ProductResponse> getProductsByWishList(final int page, final int size) {
        final Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        final Page<Product> productPage = productWishListRepository.getPopularProductsByWishList(pageable);

        return productPage.stream().map(ProductResponse::from).toList();
    }

    @Override
    public boolean isProductWishedByUser(final User user, final Long productId) {

        return (user != null) && productWishListRepository.isProductWishedByUser(user.getId(), productId);
    }
}
