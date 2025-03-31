//package com.jishop.category.service.Impl;
//
//import com.jishop.category.dto.SubCategoryResponse;
//import com.jishop.category.service.CategoryRedisService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class CategoryRedisServiceImpl implements CategoryRedisService {
//
//    private final RedisTemplate<String, Object> redisTemplate;
//
//    private static final String CATEGORY_REDIS_KEY_PREFIX = "category:";
//
//    private static final String SUBCATEGORY_REDIS_KEY_SUFFIX = ":subcategories";
//
//    private static final long REDIS_TTL_HOURS = 24;
//
////    @Override
////    @SuppressWarnings("unchecked")
////    public List<SubCategoryResponse> getSubCategoriesFromRedis(final Long categoryId) {
////        final String redisKey = generateCategoryIdRedisKey(categoryId);
////        try {
////            return (List<SubCategoryResponse>) redisTemplate.opsForValue().get(redisKey);
////        } catch (Exception e) {
////            log.error("Redis 캐시 조회 실패 (카테고리 ID: {}): {}", categoryId, e.getMessage(), e);
////            return null;
////        }
////    }
//
////    @Override
////    public void saveSubCategoriesToRedis(final Long categoryId, final List<SubCategoryResponse> subCategories) {
////        final String redisKey = generateCategoryIdRedisKey(categoryId);
////        try {
////            redisTemplate.opsForValue().set(redisKey, subCategories, REDIS_TTL_HOURS, TimeUnit.HOURS);
////        } catch (Exception e) {
////            log.error("Redis 캐시 저장 실패 (카테고리 ID: {}): {}", categoryId, e.getMessage(), e);
////        }
////    }
//
////    @Override
////    public void saveCategoryName() {
////        final String redisKey = generateCategoryIdRedisKey();
////        try {
////            redisTemplate.opsForValue().set(redisKey, , REDIS_TTL_HOURS, TimeUnit.HOURS);
////        } catch (Exception e) {
////            log.error("Redis 캐시 저장 실패 (카테고리 ID: {}): {}", categoryId, e.getMessage(), e);
////        }
////    }
//
////    private String generateDropdownRedisKey() {
////        return null;
////    }
//
////    private String generateCategoryIdRedisKey(final Long categoryId) {
////        return CATEGORY_REDIS_KEY_PREFIX + categoryId + SUBCATEGORY_REDIS_KEY_SUFFIX;
////    }
//}
