package com.jishop.category.service;

import com.jishop.category.dto.SubCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryCacheServiceImpl implements CategoryCacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String SUBCATEGORY_CACHE_KEY_PREFIX = "category:";
    private static final String SUBCATEGORY_CACHE_KEY_SUFFIX = ":subcategories";
    private static final long CACHE_TTL_HOURS = 24;

    @Override
    @SuppressWarnings("unchecked")
    public List<SubCategory> getSubCategoriesFromCache(Long categoryId) {
        String cacheKey = generateCacheKey(categoryId);
        try {
            return (List<SubCategory>) redisTemplate.opsForValue().get(cacheKey);
        } catch (Exception e) {
            log.error("Redis 캐시 조회 실패 (카테고리 ID: {}): {}", categoryId, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void saveSubCategoriesToCache(Long categoryId, List<SubCategory> subCategories) {
        String cacheKey = generateCacheKey(categoryId);
        try {
            redisTemplate.opsForValue().set(cacheKey, subCategories, CACHE_TTL_HOURS, TimeUnit.HOURS);
        } catch (Exception e) {
            log.error("Redis 캐시 저장 실패 (카테고리 ID: {}): {}", categoryId, e.getMessage(), e);
        }
    }

    private String generateCacheKey(Long parentId) {
        return SUBCATEGORY_CACHE_KEY_PREFIX + parentId + SUBCATEGORY_CACHE_KEY_SUFFIX;
    }
}
