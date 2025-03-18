package com.jishop.order.repository;

import com.jishop.order.domain.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    @Query("select od from OrderDetail od " +
            "join fetch od.saleProduct sp " +
            "left join fetch sp.option o " +
            "join fetch sp.product p " +
            "where od.id = :orderDetailId")
    Optional<OrderDetail> findOrderDetailForReviewById(@Param("orderDetailId") Long orderDetailId);
}