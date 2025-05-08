package com.jishop.product.service.impl;

import com.jishop.category.repository.CategoryRepository;
import com.jishop.common.response.PageResponse;
import com.jishop.product.domain.Product;
import com.jishop.product.dto.response.ProductResponse;
import com.jishop.product.service.ProductCategoryService;
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
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public PageResponse<ProductResponse> getProductsByCategory(final Long categoryId, final int page, final int size) {
        final Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "wishListCount"));
        final Page<Product> productPage = categoryRepository
                .findProductsByCategoryWithAllDescendants(categoryId, pageable);

        final List<ProductResponse> productsResponse = productPage.getContent().stream()
                .map(ProductResponse::from)
                .toList();

        return PageResponse.from(
                productsResponse,
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements()
        );
    }

    @Override
    public List<Long> getCategoryIdsWithSubcategories(final Long categoryId) {
        if (categoryId == null) return List.of();

        return categoryRepository.findById(categoryId)
                .map(category -> {
                    final List<Long> allSubCategoryIds = categoryRepository.findAllSubCategoryIds(categoryId);
                    final List<Long> subCategoryPKs = categoryRepository.findIdsByCurrentIds(allSubCategoryIds);

                    return subCategoryPKs.isEmpty()
                            ? List.of(category.getId())
                            : subCategoryPKs;
                })
                .orElse(List.of());
    }
}
