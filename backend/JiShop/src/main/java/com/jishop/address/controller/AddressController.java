package com.jishop.address.controller;

import com.jishop.address.dto.AddressRequest;
import com.jishop.address.dto.AddressResponse;
import com.jishop.member.domain.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "배송지 API")
public interface AddressController {

    ResponseEntity<String> addAddress(User user, AddressRequest request);
    List<AddressResponse> getAddressList(User user);
    AddressResponse getAddress(Long id);
    ResponseEntity<String> updateAddress(User user, Long id, AddressRequest request);
    ResponseEntity<String> deleteAddress(Long id);
}
