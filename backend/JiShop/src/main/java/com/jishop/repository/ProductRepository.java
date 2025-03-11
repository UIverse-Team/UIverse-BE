package com.jishop.repository;

import com.jishop.domain.product.BatchProductData;
import com.jishop.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
