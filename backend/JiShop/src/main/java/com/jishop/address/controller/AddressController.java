package com.jishop.address.controller;

import com.jishop.address.dto.AddressRequest;
import com.jishop.address.dto.AddressResponse;
import com.jishop.member.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "배송지 API")
public interface AddressController {

    @Operation(summary = "배송지 추가")
    ResponseEntity<String> addAddress(User user, AddressRequest request);
    @Operation(summary = "배송지 목록 조회")
    List<AddressResponse> getAddressList(User user);
    @Operation(summary = "배송지 상세")
    AddressResponse getAddress(Long id);
    @Operation(summary = "배송지 수정")
    ResponseEntity<String> updateAddress(User user, Long id, AddressRequest request);
    @Operation(summary = "배송지 삭제")
    ResponseEntity<String> deleteAddress(Long id);
    @Operation(summary = "기본 배송지 가져오기")
    ResponseEntity<?> getDefaultAddress(User user);
}
