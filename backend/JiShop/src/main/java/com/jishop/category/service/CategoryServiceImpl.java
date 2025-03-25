package com.jishop.category.service;

import com.jishop.category.domain.Category;
import com.jishop.category.dto.CategoryResponse;
import com.jishop.category.dto.SubCategory;
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
    private final CategoryCacheService categoryCacheService;

    @Override
    public PagedModel<CategoryResponse> getProductsByCategory(Long categoryId, int page) {
        Category category = categoryRepository.findByCategoryId(categoryId)
                .orElseThrow(() -> new DomainException(ErrorType.CATEGORY_NOT_FOUND));

        List<SubCategory> subCategories = getSubCategories(categoryId);

        Pageable pageable = PageRequest.of(page, 12, Sort.by(Sort.Direction.DESC, "wishListCount"));
        Page<Product> productPage = categoryRepository.findProductsByCategoryWithAllDescendants(categoryId, pageable);

        CategoryResponse categoryResponse = CategoryResponse.from(
                categoryId,
                category.getName(),
                subCategories,
                productPage.getContent()
        );

        return new PagedModel<>(new PageImpl<>(
                List.of(categoryResponse),
                pageable,
                productPage.getTotalElements()
        ));
    }

    private List<SubCategory> getSubCategories(Long categoryId) {
        List<SubCategory> cachedData = categoryCacheService.getSubCategoriesFromCache(categoryId);
        if (cachedData != null) {
            return cachedData;
        }

        List<SubCategory> noneCachedData = getSubCategoriesByCategoryId(categoryId);
        categoryCacheService.saveSubCategoriesToCache(categoryId, noneCachedData);

        return noneCachedData;
    }

    private List<SubCategory> getSubCategoriesByCategoryId(Long categoryId) {
        List<Category> subCategories = categoryRepository.findSubcategoriesByCategoryId(categoryId);

        return subCategories.stream()
                .map(subCategory -> {
                    long productCount = categoryRepository.countProductsByCategoryId(subCategory.getCurrentId());

                    return new SubCategory(
                            subCategory.getCurrentId(),
                            subCategory.getName(),
                            productCount
                    );
                })
                .toList();
    }
}
