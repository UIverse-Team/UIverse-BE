package com.jishop.order.repository;

import com.jishop.order.domain.OrderNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderNumberRepository extends JpaRepository<OrderNumber, Long> {

    boolean existsByOrderNumber(String orderNumber);
    Optional<OrderNumber> findByOrderNumber(String orderNumberStr);
}
