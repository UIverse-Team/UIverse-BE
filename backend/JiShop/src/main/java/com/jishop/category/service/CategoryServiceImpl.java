package com.jishop.category.service;

import com.jishop.category.domain.Category;
import com.jishop.category.dto.CategoryResponse;
import com.jishop.category.dto.HeaderProductListResponse;
import com.jishop.category.dto.SubCategoryResponse;
import com.jishop.category.repository.CategoryRepository;
import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.product.domain.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryRedisService categoryRedisService;

    @Override
    public PagedModel<HeaderProductListResponse> getProductsByCategory(Long categoryId, int page) {
        Category category = categoryRepository.findByCategoryId(categoryId)
                .orElseThrow(() -> new DomainException(ErrorType.CATEGORY_NOT_FOUND));

        List<SubCategoryResponse> subCategories = getSubCategories(categoryId);

        Pageable pageable = PageRequest.of(page, 12, Sort.by(Sort.Direction.DESC, "wishListCount"));
        Page<Product> productPage = categoryRepository.findProductsByCategoryWithAllDescendants(categoryId, pageable);

        HeaderProductListResponse headerProductListResponse = HeaderProductListResponse.from(
                CategoryResponse.from(category),
                subCategories,
                productPage.getContent()
        );

        return new PagedModel<>(new PageImpl<>(
                List.of(headerProductListResponse),
                pageable,
                productPage.getTotalElements()
        ));
    }

    private List<SubCategoryResponse> getSubCategories(Long categoryId) {
        List<SubCategoryResponse> cachedData = categoryRedisService.getSubCategoriesFromRedis(categoryId);
        if (cachedData != null) {
            return cachedData;
        }

        List<SubCategoryResponse> noneCachedData = getSubCategoriesByCategoryId(categoryId);
        categoryRedisService.saveSubCategoriesToRedis(categoryId, noneCachedData);

        return noneCachedData;
    }

    private List<SubCategoryResponse> getSubCategoriesByCategoryId(Long categoryId) {
        List<Category> subCategories = categoryRepository.findSubcategoriesByCategoryId(categoryId);

        return subCategories.stream()
                .map(subCategory -> {
                    long productCount = categoryRepository.countProductsByCategoryId(subCategory.getCurrentId());

                    return new SubCategoryResponse(
                            subCategory.getCurrentId(),
                            subCategory.getName(),
                            productCount
                    );
                })
                .toList();
    }
}
