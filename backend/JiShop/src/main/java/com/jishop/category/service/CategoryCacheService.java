package com.jishop.category.service;

import com.jishop.category.dto.SubCategory;

import java.util.List;

public interface CategoryCacheService {

    List<SubCategory> getSubCategoriesFromCache(Long parentId);

    void saveSubCategoriesToCache(Long parentId, List<SubCategory> subCategories);
}
