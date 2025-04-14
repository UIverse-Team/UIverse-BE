package com.jishop.product.service.impl;

import com.jishop.category.repository.CategoryRepository;
import com.jishop.product.domain.Product;
import com.jishop.product.dto.response.ProductResponse;
import com.jishop.product.service.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public PagedModel<ProductResponse> getProductsByCategory(final Long categoryId, final int page, final int size) {

        final Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "wishListCount"));
        final Page<Product> productPage = categoryRepository.findProductsByCategoryWithAllDescendants(categoryId, pageable);

        final List<ProductResponse> productsResponse = productPage.getContent().stream()
                .map(ProductResponse::from)
                .toList();

        return new PagedModel<>(new PageImpl<>(
                productsResponse,
                pageable,
                productPage.getTotalElements()
        ));
    }
}
