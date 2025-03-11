package com.jishop.product.repository;

import com.jishop.product.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Integer> {
    Optional<Stock> findBySaleProductId(Long saleProductId);
}
