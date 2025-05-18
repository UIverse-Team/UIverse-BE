package com.jiseller.store.repository;

import com.jiseller.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findByMallSeq(Long mallSeq);

}
