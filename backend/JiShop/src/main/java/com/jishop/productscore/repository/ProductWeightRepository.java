package com.jishop.productscore.repository;

import com.jishop.productscore.domain.ProductWeight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductWeightRepository extends JpaRepository<ProductWeight, Long> {
}
