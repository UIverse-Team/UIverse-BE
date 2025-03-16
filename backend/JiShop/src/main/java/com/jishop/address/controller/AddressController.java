package com.jishop.address.controller;

import com.jishop.address.dto.AddressRequest;
import com.jishop.address.dto.AddressResponse;
import com.jishop.member.domain.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AddressController {

    ResponseEntity<String> addAddress(User user, AddressRequest request);
    List<AddressResponse> getAddressList(User user);
    AddressResponse getAddress(Long id);

}
