package com.jishop.wishlist.repository;

import com.jishop.product.domain.Product;
import com.jishop.wishlist.domain.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishListRepository extends JpaRepository<WishList, Long> {

    Optional<WishList> findByProduct(Product product);
}
