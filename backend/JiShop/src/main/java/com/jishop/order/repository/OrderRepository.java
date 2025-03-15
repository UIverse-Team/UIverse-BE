package com.jishop.order.repository;

import com.jishop.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
            "WHERE o.userId = :userId")
    List<Order> findAllWithDetails(@Param("userId") Long userId);

    boolean existsByOrderNumber(String orderNumber);
}
