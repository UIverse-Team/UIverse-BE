package com.jishop.address.controller;

import com.jishop.address.dto.AddressRequest;
import com.jishop.address.dto.AddressResponse;
import com.jishop.address.service.AddressService;
import com.jishop.member.annotation.CurrentUser;
import com.jishop.member.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/address")
public class AddressControllerImpl implements AddressController {

    private final AddressService addressService;

    @PostMapping("/addaddress")
    public ResponseEntity<String> addAddress(@CurrentUser User user, @RequestBody AddressRequest request) {
        addressService.addAddress(user, request);
        return ResponseEntity.ok("배송지 추가 완료!");
    }

    // todo: 배송지 전체 조회 (최대 개수 정해야하나?)
    @GetMapping("/getaddressList")
    public List<AddressResponse> getAddressList(@CurrentUser User user) {
        return addressService.getAddressList(user);
    }
    // todo: 배송지 단일 조회
    // @GetMapping("/getaddress")

    // todo: 배송지 삭제

    // todo: 배송지 변경

}
