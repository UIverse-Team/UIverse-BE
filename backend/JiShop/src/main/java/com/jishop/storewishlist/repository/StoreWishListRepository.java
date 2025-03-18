package com.jishop.storewishlist.repository;

import com.jishop.member.domain.User;
import com.jishop.store.domain.Store;
import com.jishop.storewishlist.domain.StoreWishList;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface StoreWishListRepository extends JpaRepository<StoreWishList, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    Optional<StoreWishList> findByUserAndStore(User user, Store store);
    List<StoreWishList> findAllByUser(User user);
}
