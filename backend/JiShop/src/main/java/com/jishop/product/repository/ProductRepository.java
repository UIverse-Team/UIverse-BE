package com.jishop.product.repository;

import com.jishop.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryQueryDsl {

    boolean existsByBrand(String keyword);

    boolean existsByNameContaining(String keyword);

    boolean existsByBrandContaining(String keyword);

    List<Product> findAllByBrand(String keyword);

    List<Product> findAllByNameContaining(String keyword);

    List<Product> findAllByBrandContaining(String keyword);
}
