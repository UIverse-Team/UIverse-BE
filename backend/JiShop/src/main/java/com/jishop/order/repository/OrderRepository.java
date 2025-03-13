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
    // 단일 주문 조회를 위한 fetch join 쿼리
    @Query("SELECT DISTINCT o FROM Order o " +
            "LEFT JOIN FETCH o.orderNumber " +
            "LEFT JOIN FETCH o.orderDetails od " +
            "LEFT JOIN FETCH od.saleProduct sp " +
            "LEFT JOIN FETCH sp.product " +
            "LEFT JOIN FETCH sp.option " +
            "WHERE o.id = :orderId")
    Optional<Order> findByIdWithDetails(@Param("orderId") Long orderId);

    // 전체 주문 조회를 위한 fetch join 쿼리
    @Query("SELECT DISTINCT o FROM Order o " +
            "LEFT JOIN FETCH o.orderNumber " +
            "LEFT JOIN FETCH o.orderDetails od " +
            "LEFT JOIN FETCH od.saleProduct sp " +
            "LEFT JOIN FETCH sp.product " +
            "LEFT JOIN FETCH sp.option")
    List<Order> findAllWithDetails();
}
