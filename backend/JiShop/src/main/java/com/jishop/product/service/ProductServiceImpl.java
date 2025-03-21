package com.jishop.product.service;

import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.member.domain.User;
import com.jishop.product.domain.Product;
import com.jishop.product.domain.QProduct;
import com.jishop.product.dto.response.ProductListResponse;
import com.jishop.product.dto.request.ProductRequest;
import com.jishop.product.dto.response.ProductResponse;
import com.jishop.product.implementation.ProductQueryHelper;
import com.jishop.product.repository.ProductRepository;
import com.jishop.product.repository.ProductRepositoryQueryDsl;
import com.jishop.reviewproduct.domain.QReviewProduct;
import com.jishop.reviewproduct.domain.ReviewProduct;
import com.jishop.reviewproduct.repository.ReviewProductRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ReviewProductRepository reviewProductRepository;
    private final ProductQueryHelper productQueryHelper;
    private final ProductRepositoryQueryDsl productRepositoryQueryDsl;

    @Override
    public PagedModel<ProductListResponse> getProductList(ProductRequest productRequest) {
        BooleanBuilder filterBuilder = productQueryHelper
                .findProductsByCondition(productRequest, QProduct.product, QReviewProduct.reviewProduct);

        OrderSpecifier<?> orderSpecifier = addSorting(productRequest.sort(), QProduct.product);

        List<Product> results = productRepositoryQueryDsl
                .getFilteredAndSortedResults(filterBuilder, orderSpecifier, productRequest);

        List<ProductListResponse> productList = results.stream()
                .map(ProductListResponse::from).collect(Collectors.toList());

        long totalCount = productRepositoryQueryDsl.countFilteredProducts(filterBuilder);

        Pageable pageable = PageRequest.of(productRequest.page(), productRequest.size());
        Page<ProductListResponse> ProductListResponsePage = new PageImpl<>(productList, pageable, totalCount);

        return new PagedModel<>(ProductListResponsePage);
    }

    @Override
    public ProductResponse getProduct(User user, Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new DomainException(ErrorType.PRODUCT_NOT_FOUND));

        Boolean isWished = false;
        if (user != null) {
            isWished = productRepository.findProductWishStatusByUserAndProduct(user, product).orElse(false);
        }

        ReviewProduct reviewProduct = reviewProductRepository.findByProduct(product).orElse(null);

        int reviewCount = 0;
        int reviewRate = 0;

        if (reviewProduct != null) {
            reviewCount = reviewProduct.getReviewCount();
            if (reviewCount > 0) {
                reviewRate = reviewProduct.getReviewScore() / reviewCount;
            }
        }

        return ProductResponse.from(product, isWished, reviewCount, reviewRate);
    }

    private OrderSpecifier<?> addSorting(String sort, QProduct product) {
        return switch (sort) {
            case "wish" -> product.wishListCount.desc();
            case "latest" -> product.createdAt.desc();
            case "priceAsc" -> product.discountPrice.asc();
            case "priceDesc" -> product.discountPrice.desc();
            case "discount" -> product.discountRate.desc();
            default -> product.wishListCount.desc();
        };
    }
}
