package com.jishop.address.controller;

import com.jishop.address.dto.AddressRequest;
import com.jishop.address.dto.AddressResponse;
import com.jishop.address.service.AddressService;
import com.jishop.member.annotation.CurrentUser;
import com.jishop.member.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/address")
public class AddressControllerImpl implements AddressController {

    private final AddressService addressService;

    @PostMapping("/add")
    public ResponseEntity<String> addAddress(@CurrentUser User user, @RequestBody @Validated AddressRequest request) {
        addressService.addAddress(user, request);
        return ResponseEntity.ok("배송지 추가 완료!");
    }

    // todo: 배송지 전체 조회 (최대 개수 정해야하나?)
    @GetMapping("/getList")
    public List<AddressResponse> getAddressList(@CurrentUser User user) {
        return addressService.getAddressList(user);
    }

    // todo: 배송지 단일 조회
    @GetMapping("/{id}")
    public AddressResponse getAddress(@PathVariable Long id) {
        return addressService.getAddress(id);
    }

    // todo: 배송지 변경
    @PutMapping("/{id}")
    public ResponseEntity<String> updateAddress(@CurrentUser User user, @PathVariable Long id,
                                                @RequestBody @Validated AddressRequest request) {
        addressService.updateAddress(user, id, request);

        return ResponseEntity.ok("배송지 변경 완료!");
    }

    // todo: 배송지 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);

        return ResponseEntity.ok("배송지가 삭제 되었습니다!");
    }

    // 기본 배송지 가져오기
    @GetMapping("/default-address")
    public ResponseEntity<?> getDefaultAddress(@CurrentUser User user) {
        Optional<AddressResponse> addressResponse = addressService.getDefaultAddress(user);

        if(addressResponse.isPresent()) {
            return ResponseEntity.ok(addressResponse.get());
        } else {
            return ResponseEntity.ok("기본배송지가 존재하지 않습니다");
        }
    }
}
