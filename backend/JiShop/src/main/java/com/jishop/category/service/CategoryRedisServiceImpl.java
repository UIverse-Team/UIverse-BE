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
public class CategoryRedisServiceImpl implements CategoryRedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String SUBCATEGORY_REDIS_KEY_PREFIX = "category:";
    private static final String SUBCATEGORY_REDIS_KEY_SUFFIX = ":subcategories";
    private static final long REDIS_TTL_HOURS = 24;

    @Override
    @SuppressWarnings("unchecked")
    public List<SubCategory> getSubCategoriesFromRedis(Long categoryId) {
        String redisKey = generateRedisKey(categoryId);
        try {
            return (List<SubCategory>) redisTemplate.opsForValue().get(redisKey);
        } catch (Exception e) {
            log.error("Redis 캐시 조회 실패 (카테고리 ID: {}): {}", categoryId, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void saveSubCategoriesToRedis(Long categoryId, List<SubCategory> subCategories) {
        String redisKey = generateRedisKey(categoryId);
        try {
            redisTemplate.opsForValue().set(redisKey, subCategories, REDIS_TTL_HOURS, TimeUnit.HOURS);
        } catch (Exception e) {
            log.error("Redis 캐시 저장 실패 (카테고리 ID: {}): {}", categoryId, e.getMessage(), e);
        }
    }

    private String generateRedisKey(Long parentId) {
        return SUBCATEGORY_REDIS_KEY_PREFIX + parentId + SUBCATEGORY_REDIS_KEY_SUFFIX;
    }
}
