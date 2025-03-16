package com.jishop.address.controller;

import com.jishop.address.dto.AddressRequest;
import com.jishop.member.domain.User;
import org.springframework.http.ResponseEntity;

public interface AddressController {

    ResponseEntity<String> addAddress(User user, AddressRequest request);
}
