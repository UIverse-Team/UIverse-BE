package com.jishop.category.service.Impl;

import com.jishop.category.domain.Category;
import com.jishop.category.dto.CategoryResponse;
import com.jishop.category.repository.CategoryRepository;
import com.jishop.category.service.CategoryService;
import com.jishop.product.domain.Product;
import com.jishop.product.dto.response.ProductResponse;
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
//    private final CategoryRedisService categoryRedisService;

    @Override
    public PagedModel<ProductResponse> getProductsByCategory(Long categoryId, int page) {

        Pageable pageable = PageRequest.of(page, 12, Sort.by(Sort.Direction.DESC, "wishListCount"));
        Page<Product> productPage = categoryRepository.findProductsByCategoryWithAllDescendants(categoryId, pageable);

        List<ProductResponse> productsResponse = productPage.getContent().stream()
                .map(ProductResponse::from)
                .toList();

        return new PagedModel<>(new PageImpl<>(
                productsResponse,
                pageable,
                productPage.getTotalElements()
        ));
    }

    @Override
    public List<CategoryResponse> getCategoryFilterInfo() {
        List<Category> topLevelCategories = categoryRepository.findTopLevelCategories();

        return topLevelCategories.stream().map(CategoryResponse::from).toList();
    }

    @Override
    public List<CategoryResponse> getSubcategoriesByParentId(Long categoryId) {
        List<Category> subcategories = categoryRepository.findSubcategoriesById(categoryId);

        return subcategories.stream().map(CategoryResponse::from).toList();
    }

    //    public List<SubCategoryResponse> getSubCategories(Long categoryId) {
//        List<SubCategoryResponse> cachedDataFromRedis = categoryRedisService.getSubCategoriesFromRedis(categoryId);
//
//        List<SubCategoryResponse> unCachedDataFromRedis = getSubCategoriesByCategoryId(categoryId);
//        categoryRedisService.saveSubCategoriesToRedis(categoryId, unCachedDataFromRedis);
//
//        return (cachedDataFromRedis != null) ? cachedDataFromRedis : unCachedDataFromRedis;
//    }
}
