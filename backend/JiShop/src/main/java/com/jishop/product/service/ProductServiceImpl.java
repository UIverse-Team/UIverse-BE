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

        if ("discount".equals(productRequest.sort())) {
            Pageable pageable = PageRequest.of(productRequest.page(), productRequest.size());
            Page<Product> productPage = productRepository.findAllOrderByDiscountRateDesc(pageable);

            return new PagedModel<>(productPage.map(ProductResponse::from));
        }

        Sort sort = switch (productRequest.sort()) {
            case "wish" -> Sort.by(Sort.Direction.DESC, "wishListCount");
            case "latest" -> Sort.by(Sort.Direction.DESC, "createdAt");
            case "priceAsc" -> Sort.by(Sort.Direction.ASC, "discountPrice");
            case "priceDesc" -> Sort.by(Sort.Direction.DESC, "discountPrice");
            default -> Sort.by(Sort.Direction.DESC, "wishListCount");
        };

        Pageable pageable = PageRequest.of(productRequest.page(), productRequest.size(), sort);
        Page<Product> productPage = productRepository.findAll(pageable);


        return new PagedModel<>(productPage.map(ProductResponse::from));
    }

    @Override
    public ProductResponse getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new DomainException(ErrorType.PRODUCT_NOT_FOUND));

        return ProductResponse.from(product);
    }
}
