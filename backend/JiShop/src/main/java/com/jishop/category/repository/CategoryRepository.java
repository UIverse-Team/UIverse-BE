package com.jishop.category.repository;

import com.jishop.category.domain.Category;
import com.jishop.product.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

    /**
     * 상위(1뎁스) 카테고리 ID로 그 하위(2,3뎁스) 카테고리 상품 목록 모두 조회
     * @param categoryId 1뎁스 카테고리 ID
     * @return 해당 카테고리에 속한 상품 페이지
     */
    @Query(value = "SELECT p FROM Product p " +
            "LEFT JOIN p.category c1 " +
            "LEFT JOIN Category c2 ON c1.parent = c2 " +
            "WHERE c1.parent.currentId = :categoryId OR " +
            "c2.parent.currentId = :categoryId",
            countQuery = "SELECT COUNT(p) FROM Product p " +
                    "LEFT JOIN p.category c1 " +
                    "LEFT JOIN Category c2 ON c1.parent = c2 " +
                    "WHERE c1.parent.currentId = :categoryId OR " +
                    "c2.parent.currentId = :categoryId")
    Page<Product> findProductsByCategoryWithAllDescendants(
            @Param("categoryId") Long categoryId, Pageable pageable);

    /**
     * 특정 카테고리의 모든 하위 카테고리 ID를 재귀적으로 조회
     * @param categoryId 상위 카테고리 ID
     * @return 해당 카테고리와 모든 하위 카테고리의 ID 목록
     */
    @Query(value = "WITH RECURSIVE CategoryCTE AS (" +
            "SELECT current_id FROM category WHERE current_id = :categoryId " +
            "UNION ALL " +
            "SELECT c.current_id FROM category c " +
            "INNER JOIN CategoryCTE cte ON c.parent_id = cte.current_id" +
            ") " +
            "SELECT current_id FROM CategoryCTE", nativeQuery = true)
    List<Long> findAllSubCategoryIds(@Param("categoryId") Long categoryId);

    @Query("SELECT c FROM Category c WHERE c.currentId = :categoryId")
    Optional<Category> findById(@Param("categoryId") Long categoryId);

    /**
     * current_id 값들로 해당하는 카테고리들의 PK(id) 조회
     * @param currentIds 조회할 카테고리 ID 목록
     * @return 해당 카테고리들의 기본키(id) 목록
     */
    @Query("SELECT c.id FROM Category c WHERE c.currentId IN :currentIds")
    List<Long> findIdsByCurrentIds(@Param("currentIds") List<Long> currentIds);

    /**
     * 상위 카테고리 ID로 한 단계 아래의 카테고리들만 조회
     * @param categoryId 상위 카테고리 ID
     * @return 직계 하위 카테고리 목록
     */
    @Query("SELECT c FROM Category c WHERE c.parent.currentId = :categoryId")
    List<Category> findSubcategoriesById(@Param("categoryId") Long categoryId);

    /**
     * 카테고리와 그 모든 하위 카테고리에 속한 상품 모든 상품개수를 카운트
     * @param categoryId 카테고리 ID
     * @return 카테고리와 모든 하위 카테고리에 속한 총 상품 개수
     */
    @Query(value = "WITH RECURSIVE CategoryHierarchy AS (" +
            "  SELECT current_id FROM category WHERE current_id = :categoryId " +
            "  UNION ALL " +
            "  SELECT c.current_id FROM category c " +
            "  JOIN CategoryHierarchy ch ON c.parent_id = ch.current_id" +
            ") " +
            "SELECT COUNT(p.id) FROM products p " +
            "JOIN category c ON p.category_id = c.id " +
            "WHERE c.current_id IN (SELECT current_id FROM CategoryHierarchy)",
            nativeQuery = true)
    long countProductsByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * 최상위 카테고리(1뎁스)와 그 하위 카테고리들을 모두 조회
     * @return 최상위 카테고리와 그 하위 카테고리들
     */
    @Query("SELECT DISTINCT c FROM Category c " +
            "LEFT JOIN FETCH c.children ch1 " +
            "LEFT JOIN FETCH ch1.children ch2 " +
            "WHERE c.parent IS NULL " +  // 부모가 없는 최상위 카테고리
            "ORDER BY c.level DESC ")
    List<Category> findAllCategories();

    /**
     * 1뎁스(최상위) 카테고리의 ID와 이름 조회
     * @return 1뎁스 카테고리 목록
     */
    @Query("SELECT c FROM Category c WHERE c.parent IS NULL ORDER BY c.id")
    List<Category> findTopLevelCategories();

    /**
     * 1뎁스 카테고리와 해당 카테고리의 직계 하위(2뎁스) 카테고리 정보를 함께 조회
     * @return 1뎁스 카테고리와 그 직계 하위 카테고리 정보
     */
    @Query("SELECT DISTINCT c FROM Category c " +
            "LEFT JOIN FETCH c.children " +
            "WHERE c.parent IS NULL " +
            "ORDER BY c.id")
    List<Category> findTopLevelCategoriesWithSubcategories();
}