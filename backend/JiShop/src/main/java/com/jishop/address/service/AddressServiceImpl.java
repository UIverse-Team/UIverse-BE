package com.jishop.address.service;

import com.jishop.address.domain.Address;
import com.jishop.address.dto.AddressRequest;
import com.jishop.address.dto.AddressResponse;
import com.jishop.address.repository.AddressRepository;
import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.member.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    public void addAddress(User user, AddressRequest request) {
        // todo: 새로 들어오는게 기본 배송지 True 라면?
        // todo: 배송지 중복 체크는 못함 절대 못함 -> 안해도됨 진짜
        // todo: 만약에 주소지가 하나도 없는데 주소 추가시 그냥 바로 기본 주소지로 되게
        List<Address> existingAddresses = addressRepository.findByUser(user);

        boolean isFirstAddress = existingAddresses.isEmpty();
        boolean makeDefault = isFirstAddress || request.defaultYN();

        if (!isFirstAddress && request.defaultYN()) changeDefault(user);

        addressRepository.save(request.toEntity(user, makeDefault));
    }

    // todo: 배송지 전체 조회 (최대 개수 정해야하나?) -> 기본 주소지는 맨 위로
    public List<AddressResponse> getAddressList(User user) {
        List<Address> addresseList = addressRepository.findAllByUser(user);

        return addresseList.stream().map(AddressResponse::fromEntity).toList();
    }

    public AddressResponse getAddress(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new DomainException(ErrorType.ADDRESS_NOT_FOUND));

        return AddressResponse.fromEntity(address);
    }

    // todo: 배송지 변경
    public void updateAddress(User user, Long addressId, AddressRequest request) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new DomainException(ErrorType.ADDRESS_NOT_FOUND));

        if(address.isDefaultYN()) {
            if(!request.defaultYN()) throw new DomainException(ErrorType.DEFAULT_ADDRESS_REQUIRED);
        } else {
            changeDefault(user);
        }
        address.updateAddress(request);
    }

    @Override
    public AddressResponse getDefaultAddress(User user) {
        return addressRepository.findDefaultAddressByUser(user)
                .map(address -> new AddressResponse(
                        address.getRecipient(),
                        address.getPhone(),
                        address.getZonecode(),
                        address.getAddress(),
                        address.getDetailAddress(),
                        address.isDefaultYN()
                ))
                .orElseThrow(() -> new DomainException(ErrorType.DEFAULTADDRESS_NOT_FOUND));
    }

    // todo: 배송지 삭제
    public void deleteAddress(Long addressId) {
        addressRepository.deleteById(addressId);
    }

    // todo: 기본배송지 여부는 메서드로 따로 빼는게 좋을듯 -> 변경, 추가 시에 사용하니까
    private void changeDefault(User user){
        // 이거 기본 주소지 없을수도 있는데 굳이?
        Address address = addressRepository.findDefaultAddressByUser(user)
                .orElseThrow(() -> new DomainException(ErrorType.DEFAULTADDRESS_NOT_FOUND));
        address.updateDefaultYN();
    }
}
