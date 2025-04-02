package com.jishop.productscore.repository;

import com.jishop.productscore.domain.ProductScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductScoreRepository extends JpaRepository<ProductScore, Long> {
    Optional<ProductScore> findByProductId(Long productId);
}
