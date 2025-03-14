package com.jishop.saleproduct.Repository;

import com.jishop.saleproduct.domain.SaleProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SaleProductRepository extends JpaRepository<SaleProduct, Long> {
    // 주문 생성에 필요한 최소한의 데이터만 조회하는 메서드
    @Query("SELECT sp FROM SaleProduct sp " +
            "LEFT JOIN FETCH sp.product p " +
            "LEFT JOIN FETCH sp.option o " +
            "LEFT JOIN FETCH sp.stock " +
            "WHERE sp.id IN :ids")
    List<SaleProduct> findAllByIdsForOrder(@Param("ids") List<Long> ids);
}