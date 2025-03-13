package com.jishop.stock.repository;

import com.jishop.stock.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findBySaleProduct_Id(Long saleProductId);
}
