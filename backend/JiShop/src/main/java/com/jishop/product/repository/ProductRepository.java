package com.jishop.product.repository;

import com.jishop.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT pw.productWishStatus FROM ProductWishList pw WHERE pw.user = :userId AND pw.product = :product")
    Optional<Boolean> findProductWishStatusByUserAndProduct(
            @Param("userId") Long userId,
            @Param("product") Product product);
}
