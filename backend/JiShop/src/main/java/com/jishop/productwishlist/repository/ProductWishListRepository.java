package com.jishop.productwishlist.repository;

import com.jishop.member.domain.User;
import com.jishop.product.domain.Product;
import com.jishop.productwishlist.domain.ProductWishList;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductWishListRepository extends JpaRepository<ProductWishList, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    Optional<ProductWishList> findByUserAndProduct(User user, Product product);
    List<ProductWishList> findAllByUser(User user);

    @Query("SELECT w.product FROM ProductWishList w " +
            "WHERE w.productWishStatus = true " +
            "AND w.product.saleStatus = 'SELLING' " +
            "AND w.product.secret = false " +
            "GROUP BY w.product " +
            "ORDER BY COUNT(w) DESC " +
            "LIMIT 10")
    List<Product> getProductByWishTopTen();

    @Query("SELECT CASE WHEN COUNT(pw) > 0 THEN true ELSE false END FROM ProductWishList pw " +
            "WHERE pw.user.id = :userId AND pw.product.id = :productId AND pw.productWishStatus = true")
    boolean isProductWishedByUser(@Param("userId") Long userId, @Param("productId") Long productId);
}
