package com.jishop.product.service;

import com.jishop.category.service.CategoryService;
import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.member.domain.User;
import com.jishop.option.dto.FashionClothesOptionResponse;
import com.jishop.option.dto.GeneralOptionResponse;
import com.jishop.order.repository.OrderDetailRepository;
import com.jishop.product.domain.DiscountStatus;
import com.jishop.product.domain.Product;
import com.jishop.product.dto.request.ProductRequest;
import com.jishop.product.dto.response.ProductDetailResponse;
import com.jishop.product.dto.response.ProductResponse;
import com.jishop.product.dto.response.TodaySpecialListResponse;
import com.jishop.product.repository.ProductRepository;
import com.jishop.productwishlist.repository.ProductWishListRepository;
import com.jishop.reviewproduct.domain.ReviewProduct;
import com.jishop.reviewproduct.repository.ReviewProductRepository;
import com.jishop.saleproduct.repository.SaleProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final CategoryService categoryService;

    private final ProductRepository productRepository;
    private final ReviewProductRepository reviewProductRepository;
    private final ProductWishListRepository productWishListRepository;
    private final SaleProductRepository saleProductRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Override
    public PagedModel<ProductResponse> getProductList(final ProductRequest productRequest,
                                                      final int page, final int size) {
        final List<Product> selectedProducts = productRepository.findProductsByCondition(productRequest, page, size);
        final List<ProductResponse> productListResponse = selectedProducts.stream()
                .map(ProductResponse::from).toList();

        final long totalCount = productRepository.countProductsByCondition(productRequest);
        final Pageable pageable = PageRequest.of(page, size);
        final Page<ProductResponse> pagedProductsResponse = new PageImpl<>(productListResponse, pageable, totalCount);

        return new PagedModel<>(pagedProductsResponse);
    }

    @Override
    public ProductDetailResponse getProduct(final User user, final Long productId) {
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
            productsOptions = fashionClothesOptions.isEmpty() ?
                              List.of() :
                              FashionClothesOptionResponse.from(fashionClothesOptions);
        } else {
            final List<Map<String, Object>> generalOptions = saleProductRepository
                    .findGeneralOptionsByProductId(productId);
            productsOptions = GeneralOptionResponse.from(generalOptions);
        }

        // 상품 리뷰
        final List<ReviewProduct> productsReviews = reviewProductRepository.findAllByProduct(product);
        final int reviewCount = productsReviews.isEmpty() ? 0 : productsReviews.get(0).getReviewCount();
        final double reviewRate = productsReviews.isEmpty() ? 0.0 : productsReviews.get(0).getAverageRating();

        return ProductDetailResponse
                .from(ProductResponse.from(product), product, isWished, reviewCount, reviewRate, productsOptions);
    }

    @Override
    public List<ProductResponse> getProductsByWishList(final int page, final int size) {
        final Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        final Page<Product> productPage = productWishListRepository.getPopularProductsByWishList(pageable);

        return productPage.stream().map(ProductResponse::from).toList();
    }

    @Override
    public PagedModel<TodaySpecialListResponse> getProductsByTodaySpecial(final int page, final int size) {
        final Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        final Page<Product> productPage = productRepository.findDailDealProducts(DiscountStatus.DAILY_DEAL, pageable);

        final Page<TodaySpecialListResponse> responsePage = productPage.map(product -> {
            final long totalSales = orderDetailRepository.countTotalOrdersByProductId(product.getId());

            return TodaySpecialListResponse.from(product, totalSales);
        });

        return new PagedModel<>(responsePage);
    }
}
