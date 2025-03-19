package com.jishop.product.service;

import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.member.domain.User;
import com.jishop.product.domain.Product;
import com.jishop.product.domain.QProduct;
import com.jishop.product.dto.ProductListResponse;
import com.jishop.product.dto.ProductRequest;
import com.jishop.product.dto.ProductResponse;
import com.jishop.product.repository.ProductRepository;
import com.jishop.productwishlist.domain.ProductWishList;
import com.jishop.reviewproduct.domain.QReviewProduct;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
    private final JPAQueryFactory queryFactory;

    @Override
    public PagedModel<ProductListResponse> getProductList(ProductRequest request) {

        BooleanBuilder filterBuilder = productRepository
                .findProductsByCondition(request, QProduct.product, QReviewProduct.reviewProduct);

        OrderSpecifier<?> orderSpecifier = addSorting(request.sort(), QProduct.product);

        List<Product> results = productRepository.getFilteredAndSortedResults(filterBuilder, orderSpecifier, request);

        long totalCount = productRepository.countFilteredProducts(filterBuilder);

        List<ProductListResponse> productList = results.stream().map(ProductListResponse::from).collect(Collectors.toList());

        Pageable pageable = PageRequest.of(request.page(), request.size());

        Page<ProductListResponse> ProductListResponsePage = new PageImpl<>(productList, pageable, totalCount);

        return new PagedModel<>(ProductListResponsePage);
    }

    @Override
    public ProductResponse getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new DomainException(ErrorType.PRODUCT_NOT_FOUND));

        return ProductResponse.from(product);
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
