package com.jishop.address.dto;

import com.jishop.address.domain.Address;
import com.jishop.member.domain.User;

public record AddressRequest(
        String zonecode,
        String address,
        String detailAddress,
        boolean defaultYN
) {
    public Address toEntity(User user){
        return Address.builder()
                .user(user)
                .zonecode(zonecode)
                .address(address)
                .detailAddress(detailAddress)
                .defaultYN(defaultYN)
                .build();
    }
}
