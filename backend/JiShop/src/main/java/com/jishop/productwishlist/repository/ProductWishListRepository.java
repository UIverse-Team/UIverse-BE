package com.jishop.productwishlist.repository;

import com.jishop.member.domain.User;
import com.jishop.product.domain.Product;
import com.jishop.productwishlist.domain.ProductWishList;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    /**
    * 위시리스트에 많이 담겨있는(인기순)순서로 조회
    */
    @Query(value = "SELECT p, COUNT(w) as wishCount " +
            "FROM Product p JOIN ProductWishList w ON p.id = w.product.id " +
            "WHERE w.productWishStatus = true " +
            "AND p.status.saleStatus = 'SELLING' " +
            "AND p.status.secret = false " +
            "GROUP BY p " +
            "ORDER BY wishCount DESC",
            countQuery = "SELECT COUNT(DISTINCT p) " +
                    "FROM Product p JOIN ProductWishList w ON p.id = w.product.id " +
                    "WHERE w.productWishStatus = true " +
                    "AND p.status.saleStatus = 'SELLING' " +
                    "AND p.status.secret = false")
    Page<Product> getPopularProductsByWishList(Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(pw) > 0 THEN true ELSE false END FROM ProductWishList pw " +
            "WHERE pw.user.id = :userId AND pw.product.id = :productId AND pw.productWishStatus = true")
    boolean isProductWishedByUser(@Param("userId") Long userId, @Param("productId") Long productId);
}
