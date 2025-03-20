package com.jishop.cart.repository;

import com.jishop.cart.domain.Cart;
import com.jishop.member.domain.User;
import com.jishop.saleproduct.domain.SaleProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByUser(User user);

    Optional<Cart> findByUserAndSaleProduct(User user, SaleProduct saleProduct);

    // JPQL을 사용하여 카트, 상품, 옵션을 한 번에 조회
    @Query("SELECT c FROM Cart c " +
            "JOIN FETCH c.saleProduct sp " +
            "JOIN FETCH sp.product p " +
            "LEFT JOIN FETCH sp.option o " +
            "WHERE c.user = :user")
    List<Cart> findCartsWithProductAndOptionByUser(@Param("user") User user);
}
