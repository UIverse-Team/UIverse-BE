package com.jishop.product.repository;

import com.jishop.product.domain.Product;
import com.jishop.product.implementation.ProductQueryHelper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductQueryHelper {

//    @Query("SELECT p FROM Product p WHERE " +
//            "(p.name LIKE %:keyword% OR " +
//            "p.description LIKE %:keyword%) " +  // 괄호로 조건 그룹화 및 여기에 공백 추가
//            "ORDER BY ((p.originPrice - p.discountPrice) / p.originPrice) DESC")
//    Page<Product> findByKeywordOrderByDiscountRateDesc(@Param("keyword") String keyword, Pageable pageable);
//
//    @Query("SELECT p FROM Product p WHERE " +
//            "p.name LIKE %:keyword% OR " +
//            "p.description LIKE %:keyword%")
//    Page<Product> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
