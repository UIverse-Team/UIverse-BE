package com.jishop.stock.repository;

import com.jishop.stock.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findBySaleProduct_Id(Long saleProductId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Stock s WHERE s.saleProduct.id = :saleProductId")
    Optional<Stock> findBySaleProduct_IdWithPessimisticLock(@Param("saleProductId") Long saleProductId);
}
