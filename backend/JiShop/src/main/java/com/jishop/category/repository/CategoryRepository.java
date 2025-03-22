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

    @Query(value = "SELECT p FROM Product p " +
                   "LEFT JOIN p.category c1 " +
                   "LEFT JOIN Category c2 ON c1.parent = c2 " +
                   "WHERE c1.parent.currentId = :topLevelCategoryId OR " +
                   "c2.parent.currentId = :topLevelCategoryId",
                    countQuery = "SELECT COUNT(p) FROM Product p " +
                                 "LEFT JOIN p.category c1 " +
                                 "LEFT JOIN Category c2 ON c1.parent = c2 " +
                                 "WHERE c1.parent.currentId = :topLevelCategoryId OR " +
                                 "c2.parent.currentId = :topLevelCategoryId")
    Page<Product> findProductsByTopLevelCategoryId(
            @Param("topLevelCategoryId") Long topLevelCategoryId, Pageable pageable);

    @Query(value = "WITH RECURSIVE CategoryCTE AS (" +
            "SELECT current_id FROM category WHERE current_id = :categoryId " +
            "UNION ALL " +
            "SELECT c.current_id FROM category c " +
            "INNER JOIN CategoryCTE cte ON c.parent_id = cte.current_id" +
            ") " +
            "SELECT current_id FROM CategoryCTE", nativeQuery = true)
    List<Long> findAllSubCategoryIds(@Param("categoryId") Long categoryId);

    @Query("SELECT c FROM Category c WHERE c.currentId = :currentId")
    Optional<Category> findByCategoryId(@Param("currentId") Long currentId);

    @Query("SELECT c.id FROM Category c WHERE c.currentId IN :currentIds")
    List<Long> findIdsByCurrentIds(@Param("currentIds") List<Long> currentIds);
}