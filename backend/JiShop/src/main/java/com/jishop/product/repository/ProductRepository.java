package com.jishop.product.repository;

import com.jishop.product.domain.DiscountStatus;
import com.jishop.product.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryQueryDsl {

    @Query("SELECT p FROM Product p WHERE p.status.discountStatus = :status AND p.status.isDiscount = true")
    Page<Product> findDailDealProducts(@Param("status") DiscountStatus status, Pageable pageable);

    boolean existsByProductInfo_Brand(String keyword);

    // "남성 셔츠" 가 검색어로 들어온 경우 +남성 +셔츠로 변환(BOOLEAN MODE)
    // "남성셔츠" 가 검색으로 들어온 경우 남성, 성셔, 셔츠로 변환(ngram parser)
    @Query(
            value = "SELECT EXISTS (SELECT 1 FROM products WHERE MATCH(name, brand) AGAINST(:keyword IN BOOLEAN MODE))",
            nativeQuery = true
    )
    Long existsByFulltext(String keyword);

    List<Product> findAllByProductInfo_Brand(String keyword);


    @Query(
            value = "SELECT * FROM products WHERE MATCH(name, brand) AGAINST(:keyword IN BOOLEAN MODE)",
            nativeQuery = true
    )
    List<Product> searchByNameOrBrandFulltext(@Param("keyword") String keyword);
}
