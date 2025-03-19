package com.jishop.productwishlist.repository;

import com.jishop.member.domain.User;
import com.jishop.product.domain.Product;
import com.jishop.productwishlist.domain.ProductWishList;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface ProductWishListRepository extends JpaRepository<ProductWishList, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    Optional<ProductWishList> findByUserAndProduct(User user, Product product);
    List<ProductWishList> findAllByUser(User user);

}
