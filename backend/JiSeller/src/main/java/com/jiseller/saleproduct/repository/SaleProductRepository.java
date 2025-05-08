package com.jiseller.saleproduct.repository;

import com.jiseller.saleproduct.domain.SaleProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleProductRepository extends JpaRepository<SaleProduct, Long> {
}
