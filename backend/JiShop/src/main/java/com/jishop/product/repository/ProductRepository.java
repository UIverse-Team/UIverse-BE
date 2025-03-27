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

    @Query("SELECT p FROM Product p WHERE p.discountStatus = :status AND p.isDiscount = true")
    Page<Product> findDailDealProducts(@Param("status") DiscountStatus status, Pageable pageable);

    boolean existsByBrand(String keyword);

    boolean existsByNameContaining(String keyword);

    boolean existsByBrandContaining(String keyword);

    List<Product> findAllByBrand(String keyword);

    List<Product> findAllByNameContaining(String keyword);

    List<Product> findAllByBrandContaining(String keyword);
}
