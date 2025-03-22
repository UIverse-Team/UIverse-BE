package com.jishop.address.service;

import com.jishop.address.dto.AddressRequest;
import com.jishop.address.dto.AddressResponse;
import com.jishop.member.domain.User;

import java.util.List;

public interface AddressService {

    void addAddress(User user, AddressRequest request);
    List<AddressResponse> getAddressList(User user);
    AddressResponse getAddress(Long addressId);
    void deleteAddress(Long addressId);
    void updateAddress(User user, Long addressId, AddressRequest request);
    AddressResponse getDefaultAddress(User user);
}
