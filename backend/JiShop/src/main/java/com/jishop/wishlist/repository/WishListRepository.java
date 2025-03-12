package com.jishop.wishlist.repository;

import com.jishop.wishlist.domain.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListRepository extends JpaRepository<WishList, Long> {
}
