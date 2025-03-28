package com.jishop.category.service;

import com.jishop.category.dto.SubCategoryResponse;

import java.util.List;

public interface CategoryRedisService {

    List<SubCategoryResponse> getSubCategoriesFromRedis(Long parentId);

    void saveSubCategoriesToRedis(Long parentId, List<SubCategoryResponse> subCategories);
}
