package com.jishop.address.repository;

import com.jishop.address.domain.Address;
import com.jishop.member.domain.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    // 쿼리 짜기 -> 주어진 유저 id이면서 default 컬럼이 True인거
    @Query("select a from Address a where a.user = :user and a.defaultYN = true")
    Optional<Address> findDefaultAddressByUserId(@Param("user") User user);

    List<Address> findAllByUser(@Param("user") User user);
}
