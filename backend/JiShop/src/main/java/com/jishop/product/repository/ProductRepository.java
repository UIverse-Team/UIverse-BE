package com.jishop.product.repository;

import com.jishop.member.domain.User;
import com.jishop.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT pw.productWishStatus FROM ProductWishList pw WHERE pw.user = :user AND pw.product = :product")
    Optional<Boolean> findProductWishStatusByUserAndProduct(
            @Param("user") User user,
            @Param("product") Product product);

    boolean existsByBrand(String keyword);

    boolean existsByNameContaining(String keyword);

    boolean existsByBrandContaining(String keyword);

    List<Product> findAllByBrand(String keyword);

    List<Product> findAllByNameContaining(String keyword);

    List<Product> findAllByBrandContaining(String keyword);
}
