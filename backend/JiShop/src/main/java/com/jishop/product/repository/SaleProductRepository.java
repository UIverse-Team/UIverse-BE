package com.jishop.product.repository;

import com.jishop.saleproduct.domain.SaleProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleProductRepository extends JpaRepository<SaleProduct, Long> {
}
