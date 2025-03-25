package com.jishop.order.repository;

import com.jishop.order.domain.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    @Query("select od from OrderDetail od " +
            "join fetch od.saleProduct sp " +
            "left join fetch sp.option o " +
            "join fetch sp.product p " +
            "where od.id = :orderDetailId")
    Optional<OrderDetail> findOrderDetailForReviewById(@Param("orderDetailId") Long orderDetailId);

    /**
     * 최근 1달 동안 상품(product) 주문량 조회
     * 
     * @param productId     조회할 상품id
     * @param startDate     조회 시작 날짜(1달전 )
     * @return 주문량 합계
     */
    @Query("SELECT COALESCE(SUM(od.quantity), 0) FROM OrderDetail od " +
            "JOIN od.saleProduct sp " +
            "JOIN sp.product p " +
            "JOIN od.order o " +
            "WHERE p.id = :productId AND o.createdAt >= :startDate")
    int countRecentOrdersByProductId(@Param("productId") Long productId,
                                     @Param("startDate") LocalDateTime startDate);

    /**
     * 특정 상품(product)의 주문량 조회
     * 
     * @param productId     조회할 상품id
     * @return 주문량 합계
     */
    @Query("SELECT COALESCE(SUM(od.quantity), 0) FROM OrderDetail od " +
            "JOIN od.saleProduct sp " +
            "JOIN sp.product p " +
            "WHERE p.id = :productId")
    int countTotalOrdersByProductId(@Param("productId") Long productId);
}