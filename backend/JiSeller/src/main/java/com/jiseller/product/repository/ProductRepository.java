package com.jiseller.product.repository;

import com.jiseller.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>  {
}
