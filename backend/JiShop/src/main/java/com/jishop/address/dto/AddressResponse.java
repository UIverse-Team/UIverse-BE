package com.jishop.address.dto;

import com.jishop.address.domain.Address;

public record AddressResponse(
        String zonecode,
        String address,
        String detailAddress,
        boolean defaultYN
) {
   public static AddressResponse fromEntity(Address address) {
       return new AddressResponse(
               address.getZonecode(),
               address.getAddress(),
               address.getDetailAddress(),
               address.isDefaultYN()
       );
   }
}
