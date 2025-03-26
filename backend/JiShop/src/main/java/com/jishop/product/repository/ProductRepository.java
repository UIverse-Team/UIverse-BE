package com.jishop.product.repository;

import com.jishop.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryQueryDsl {
}
