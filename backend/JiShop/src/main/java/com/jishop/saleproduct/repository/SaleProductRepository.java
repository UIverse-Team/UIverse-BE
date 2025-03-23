package com.jishop.saleproduct.repository;

import com.jishop.saleproduct.domain.SaleProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SaleProductRepository extends JpaRepository<SaleProduct, Long> {
    // 검색어 유효성 검증 - 상품 데이터와 관련있는지 확인
    boolean existsByNameContaining(String keyword);

    // 주문 생성에 필요한 최소한의 데이터만 조회하는 메서드
    @Query("SELECT sp FROM SaleProduct sp " +
            "LEFT JOIN FETCH sp.product p " +
            "LEFT JOIN FETCH sp.option o " +
            "LEFT JOIN FETCH sp.stock " +
            "WHERE sp.id IN :ids")
    List<SaleProduct> findAllByIdsForOrder(@Param("ids") List<Long> ids);

    @Query("SELECT new map(" +
            "sp.id as saleProductId, " +
            "o.categoryType as categoryType, " +
            "o.optionValue as optionValue, " +
            "o.optionExtra as optionExtra) " +
            "FROM SaleProduct sp " +
            "JOIN sp.option o " +
            "WHERE sp.product.id = :productId " +
            "AND sp.deleteStatus = false")
    List<Map<String, Object>> findOptionDetailsForProduct(@Param("productId") Long productId);
}
