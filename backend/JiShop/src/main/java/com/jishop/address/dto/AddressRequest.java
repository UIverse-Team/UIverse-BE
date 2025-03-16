package com.jishop.address.dto;

import com.jishop.address.domain.Address;
import com.jishop.member.domain.User;
import jakarta.validation.constraints.NotBlank;

public record AddressRequest(
        @NotBlank
        String recipient,
        @NotBlank
        String phone,
        @NotBlank
        String zonecode,
        @NotBlank
        String address,
        @NotBlank
        String detailAddress,
        boolean defaultYN
) {
    public Address toEntity(User user, boolean defaultYN) {
        return Address.builder()
                .user(user)
                .recipient(recipient)
                .phone(phone)
                .zonecode(zonecode)
                .address(address)
                .detailAddress(detailAddress)
                .defaultYN(defaultYN)
                .build();
    }
}
