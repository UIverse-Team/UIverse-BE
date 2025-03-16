package com.jishop.product.service;

import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.product.domain.Product;
import com.jishop.product.dto.ProductRequest;
import com.jishop.product.dto.ProductResponse;
import com.jishop.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public PagedModel<ProductResponse> getProductList(ProductRequest productRequest) {
        Page<Product> productPage;

        // discount 정렬 처리
        if ("discount".equals(productRequest.sort())) {
            Pageable pageable = PageRequest.of(productRequest.page(), productRequest.size());
            productPage = productRepository.findAllOrderByDiscountRateDesc(pageable);
        } else {
            // 그 외 정렬 조건 처리
            Sort sort;
            switch (productRequest.sort()) {
                case "wish":
                    sort = Sort.by(Sort.Direction.DESC, "wishListCount");
                    break;
                case "latest":
                    sort = Sort.by(Sort.Direction.DESC, "createdAt");
                    break;
                case "priceAsc":
                    sort = Sort.by(Sort.Direction.ASC, "discountPrice");
                    break;
                case "priceDesc":
                    sort = Sort.by(Sort.Direction.DESC, "discountPrice");
                    break;
                default:
                    sort = Sort.by(Sort.Direction.DESC, "wishListCount");
                    break;
            }
            Pageable pageable = PageRequest.of(productRequest.page(), productRequest.size(), sort);
            productPage = productRepository.findAll(pageable);
        }

        return new PagedModel<>(productPage.map(ProductResponse::from));
    }

    @Override
    public ProductResponse getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new DomainException(ErrorType.PRODUCT_NOT_FOUND));

        return ProductResponse.from(product);
    }
}
