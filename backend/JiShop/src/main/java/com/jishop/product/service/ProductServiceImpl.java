package com.jishop.product.service;

import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.member.domain.User;
import com.jishop.option.dto.FashionClothesOptionResponse;
import com.jishop.option.dto.GeneralOptionResponse;
import com.jishop.product.domain.Product;
import com.jishop.product.dto.request.ProductRequest;
import com.jishop.product.dto.response.ProductListResponse;
import com.jishop.product.dto.response.ProductResponse;
import com.jishop.product.repository.ProductRepository;
import com.jishop.productwishlist.repository.ProductWishListRepository;
import com.jishop.reviewproduct.domain.ReviewProduct;
import com.jishop.reviewproduct.repository.ReviewProductRepository;
import com.jishop.saleproduct.repository.SaleProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ReviewProductRepository reviewProductRepository;
    private final ProductWishListRepository productWishListRepository;
    private final SaleProductRepository saleProductRepository;

    @Override
    public PagedModel<ProductListResponse> getProductList(final ProductRequest productRequest,
                                                          final int page, final int size) {
        final List<Product> results = productRepository.findProductsByCondition(productRequest, page, size);
        final List<ProductListResponse> productList = results.stream()
                .map(ProductListResponse::from).toList();

        final long totalCount = productRepository.countProductsByCondition(productRequest);
        final Pageable pageable = PageRequest.of(page, size);
        final Page<ProductListResponse> ProductListResponsePage = new PageImpl<>(productList, pageable, totalCount);

        return new PagedModel<>(ProductListResponsePage);
    }

    @Override
    public ProductResponse getProduct(final User user, final Long productId) {
        final Product product = productRepository.findById(productId)
                .orElseThrow(() -> new DomainException(ErrorType.PRODUCT_NOT_FOUND));

        // 찜 상태
        final boolean isWished = (user != null) && productWishListRepository
                .isProductWishedByUser(user.getId(), productId);

        // 상품 옵션
        final Long categoryType = product.getLCatId();
        final Object productsOptions;
        if (categoryType == 50000000L) {
            final List<Map<String, Object>> fashionClothesOptions = saleProductRepository
                    .findFashionClothesOptionsByProductId(productId);
            productsOptions = FashionClothesOptionResponse.from(fashionClothesOptions);
        } else {
            final List<Map<String, Object>> generalOptions = saleProductRepository
                    .findGeneralOptionsByProductId(productId);
            productsOptions = GeneralOptionResponse.from(generalOptions);
        }

        // 상품 리뷰
        final ReviewProduct reviewProduct = reviewProductRepository.findByProduct(product).orElse(null);
        final int reviewCount = reviewProduct != null ? reviewProduct.getReviewCount() : 0;
        final double reviewRate = reviewProduct != null ? reviewProduct.getAverageRating() : 0.0;

        return ProductResponse.from(product, isWished, reviewCount, reviewRate, productsOptions);
    }

    @Override
    public List<ProductListResponse> getProductByWishTopTen() {
        final List<Product> products = productWishListRepository.getProductByWishTopTen();

        return products.stream().map(ProductListResponse::from).toList();
    }
}