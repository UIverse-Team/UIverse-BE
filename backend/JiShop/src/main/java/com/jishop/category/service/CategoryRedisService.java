package com.jishop.category.service;

import com.jishop.category.dto.SubCategory;

import java.util.List;

public interface CategoryRedisService {

    List<SubCategory> getSubCategoriesFromRedis(Long parentId);

    void saveSubCategoriesToRedis(Long parentId, List<SubCategory> subCategories);
}
