package com.jishop.product.service;

import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.option.domain.OptionCategory;
import com.jishop.product.domain.Product;
import com.jishop.product.domain.QProduct;
import com.jishop.product.dto.request.ProductRequest;
import com.jishop.product.dto.response.ProductListResponse;
import com.jishop.product.dto.response.ProductResponse;
import com.jishop.product.implementation.ProductQueryHelper;
import com.jishop.product.repository.ProductRepository;
import com.jishop.product.repository.ProductRepositoryQueryDsl;
import com.jishop.productwishlist.repository.ProductWishListRepository;
import com.jishop.reviewproduct.domain.QReviewProduct;
import com.jishop.reviewproduct.domain.ReviewProduct;
import com.jishop.reviewproduct.repository.ReviewProductRepository;
import com.jishop.saleproduct.repository.SaleProductRepository;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ReviewProductRepository reviewProductRepository;
    private final ProductQueryHelper productQueryHelper;
    private final ProductRepositoryQueryDsl productRepositoryQueryDsl;
    private final ProductWishListRepository productWishListRepository;
    private final SaleProductRepository saleProductRepository;

    @Override
    public PagedModel<ProductListResponse> getProductList(ProductRequest productRequest, int page, int size) {
        BooleanBuilder filterBuilder = productQueryHelper
                .findProductsByCondition(productRequest, QProduct.product, QReviewProduct.reviewProduct);

        OrderSpecifier<?> orderSpecifier = addSorting(productRequest.sort(), QProduct.product);

        List<Product> results = productRepositoryQueryDsl
                .getFilteredAndSortedResults(filterBuilder, orderSpecifier, productRequest, page, size);

        List<ProductListResponse> productList = results.stream()
                .map(ProductListResponse::from).collect(Collectors.toList());

        long totalCount = productRepositoryQueryDsl.countFilteredProducts(filterBuilder);

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductListResponse> ProductListResponsePage = new PageImpl<>(productList, pageable, totalCount);

        return new PagedModel<>(ProductListResponsePage);
    }

    @Override
    public ProductResponse getProduct(Long userId, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new DomainException(ErrorType.PRODUCT_NOT_FOUND));

        Boolean isWished = false;
        if (userId != null) {
            isWished = productRepository.findProductWishStatusByUserAndProduct(userId, product).orElse(false);
        }

        List<Map<String, Object>> productOptions = saleProductRepository.findOptionsForProduct(productId);
        Object processedOptions;
        if (!productOptions.isEmpty()) {
            OptionCategory categoryType = (OptionCategory) productOptions.get(0).get("categoryType");

            if (OptionCategory.FASHION_CLOTHES.equals(categoryType)) {
                Map<String, List<Map<String, Object>>> colorSizeMap = new HashMap<>();

                for (Map<String, Object> option : productOptions) {
                    String optionValue = (String) option.get("optionValue");
                    String[] splitValue = optionValue.split("/");

                    if (splitValue.length == 2) {
                        String color = splitValue[0];
                        String size = splitValue[1];

                        if (!colorSizeMap.containsKey(color)) {
                            colorSizeMap.put(color, new ArrayList<>());
                        }

                        Map<String, Object> sizeInfo = new HashMap<>();
                        sizeInfo.put("saleProductId", option.get("saleProductId"));
                        sizeInfo.put("size", size);
                        sizeInfo.put("extra", option.get("optionExtra"));

                        colorSizeMap.get(color).add(sizeInfo);
                    }
                }

                processedOptions = colorSizeMap;
            }
            else {
                List<Map<String, Object>> optionsList = new ArrayList<>();

                for (Map<String, Object> option : productOptions) {
                    Map<String, Object> optionInfo = new HashMap<>();
                    optionInfo.put("saleProductId", option.get("saleProductId"));
                    optionInfo.put("value", option.get("optionValue"));
                    optionInfo.put("extra", option.get("optionExtra"));
                    optionsList.add(optionInfo);
                }

                processedOptions = optionsList;
            }
        } else {
            processedOptions = new ArrayList<>();
        }

        ReviewProduct reviewProduct = reviewProductRepository.findByProduct(product).orElse(null);
        int reviewCount = 0;
        double reviewRate = 0.0;
        if (reviewProduct != null) {
            reviewCount = reviewProduct.getReviewCount();
            reviewRate = reviewProduct.getAverageRating();
        }

        return ProductResponse.from(product, isWished, reviewCount, reviewRate, processedOptions);
    }

    @Override
    public List<ProductListResponse> getProductByWishTopTen() {
        List<Product> products = productWishListRepository.getProductByWishTopTen();

        return products.stream().map(ProductListResponse::from).collect(Collectors.toList());
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
