package com.jishop.product.service.impl;

import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.member.domain.User;
import com.jishop.option.dto.FashionClothesOptionResponse;
import com.jishop.option.dto.GeneralOptionResponse;
import com.jishop.option.dto.SizeOption;
import com.jishop.product.domain.Product;
import com.jishop.product.dto.request.ProductRequest;
import com.jishop.product.dto.response.ProductDetailResponse;
import com.jishop.product.dto.response.ProductResponse;
import com.jishop.product.repository.ProductRepository;
import com.jishop.product.service.ProductCategoryService;
import com.jishop.product.service.ProductService;
import com.jishop.product.service.ProductWishlistService;
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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ReviewProductRepository reviewProductRepository;
    private final SaleProductRepository saleProductRepository;

    private final ProductWishlistService wishlistService;
    private final ProductCategoryService productCategoryService;

    @Override
    public PagedModel<ProductResponse> getProductList(
            final ProductRequest productRequest, final int page, final int size) {
        final List<Long> categoryIds = productCategoryService
                .getCategoryIdsWithSubcategories(productRequest.categoryId());

        final List<Product> selectedProducts = productRepository
                .findProductsByCondition(productRequest, page, size, categoryIds);
        final List<ProductResponse> productListResponse = selectedProducts.stream()
                .map(ProductResponse::from).toList();

        final long totalCount = productRepository.countProductsByCondition(productRequest, categoryIds);
        final Pageable pageable = PageRequest.of(page, size);
        final Page<ProductResponse> pagedProductsResponse = new PageImpl<>(productListResponse, pageable, totalCount);

        return new PagedModel<>(pagedProductsResponse);
    }

    @Override
    public ProductDetailResponse getProduct(final User user, final Long productId) {
        final Product product = productRepository.findById(productId)
                .orElseThrow(() -> new DomainException(ErrorType.PRODUCT_NOT_FOUND));

        final boolean isWished = wishlistService.isProductWishedByUser(user, productId);

        final Long categoryType = product.getCategoryInfo().getLCatId();

        final Object productsOptions;
        if (categoryType == 50000000L) {
            final List<SizeOption> fashionClothesOptions = saleProductRepository
                    .findFashionClothesOptionsByProductId(productId);
            productsOptions = fashionClothesOptions.isEmpty() ?
                    List.of() :
                    FashionClothesOptionResponse.from(fashionClothesOptions);
        } else {
            final List<SizeOption> generalOptions = saleProductRepository
                    .findGeneralOptionsByProductId(productId);
            productsOptions = GeneralOptionResponse.from(generalOptions);
        }

        final List<ReviewProduct> productsReviews = reviewProductRepository.findAllByProduct(product);
        final int reviewCount = productsReviews.isEmpty() ? 0 : productsReviews.get(0).getReviewCount();
        final double reviewRate = productsReviews.isEmpty() ? 0.0 : productsReviews.get(0).getAverageRating();

        return ProductDetailResponse
                .from(ProductResponse.from(product), product, isWished, reviewCount, reviewRate, productsOptions);
    }
}
