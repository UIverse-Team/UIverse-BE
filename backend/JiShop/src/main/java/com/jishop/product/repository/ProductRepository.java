package com.jishop.product.repository;

import com.jishop.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryQueryDsl {

    boolean existsByNameContaining(String keyword);

    List<Product> findByNameContaining(String keyword);
}
