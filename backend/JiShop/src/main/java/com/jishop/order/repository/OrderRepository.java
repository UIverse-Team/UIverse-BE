package com.jishop.order.repository;

import com.jishop.order.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // 특정 사용자의 단일 주문 조회 (fetch join 적용)
    @Query("SELECT DISTINCT o FROM Order o " +
            "LEFT JOIN FETCH o.orderDetails od " +
            "WHERE o.id = :orderId AND o.userId = :userId")
    Optional<Order> findByIdWithDetails(@Param("userId") Long userId, @Param("orderId") Long orderId);

    @Query("SELECT DISTINCT o FROM Order o " +
            "LEFT JOIN FETCH o.orderDetails od " +
            "LEFT JOIN FETCH od.saleProduct sp " +
            "LEFT JOIN FETCH sp.option " +
            "LEFT JOIN FETCH sp.product " +
            "LEFT JOIN FETCH sp.stock " +
            "WHERE o.id = :orderId AND o.userId = :userId")
    Optional<Order> findByIdWithDetailsAndProducts(@Param("userId") Long userId, @Param("orderId") Long orderId);

    // 페이징을 위한 첫 번째 쿼리: 페이징된 ID 목록 가져오기
    @Query("SELECT o.id FROM Order o " +
            "WHERE o.userId = :userId " +
            "AND (:period = 'all' OR (o.createdAt >= :startDate AND o.createdAt <= :endDate))")
    Page<Long> findOrderIdsByPeriod(@Param("userId") Long userId,
                                    @Param("period") String period,
                                    @Param("startDate") LocalDateTime startDate,
                                    @Param("endDate") LocalDateTime endDate,
                                    Pageable pageable);

    // 페이징을 위한 두 번째 쿼리: ID 목록으로 상세 데이터 조회
    @Query("SELECT DISTINCT o FROM Order o " +
            "LEFT JOIN FETCH o.orderDetails od " +
            "LEFT JOIN FETCH od.saleProduct sp " +
            "LEFT JOIN FETCH sp.option " +
            "LEFT JOIN FETCH sp.product " +
            "LEFT JOIN FETCH sp.stock " +
            "WHERE o.id IN :ids " +
            "ORDER BY o.createdAt DESC")
    List<Order> findOrdersWithDetailsByIds(@Param("ids") List<Long> ids);

    boolean existsByOrderNumber(String orderNumber);

    Optional<Order> findByOrderNumberAndPhone(String orderNumber, String phone);
}
