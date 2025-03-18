package com.jishop.address.domain;

import com.jishop.address.dto.AddressRequest;
import com.jishop.common.util.BaseEntity;
import com.jishop.member.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name="addresses")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address extends BaseEntity {

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String recipient;

    private String phone;

    @Column(name="zone_code")
    private String zonecode;

    private String address;

    @Column(name="detail_address")
    private String detailAddress;

    private boolean defaultYN;

    @Builder
    public Address(User user, String recipient, String phone, String zonecode, String address, String detailAddress, boolean defaultYN) {
        this.user = user;
        this.recipient = recipient;
        this.phone = phone;
        this.zonecode = zonecode;
        this.address = address;
        this.detailAddress = detailAddress;
        this.defaultYN = defaultYN;
    }

    public void updateDefaultYN() {
        this.defaultYN = false;
    }

    public void updateAddress(AddressRequest request) {
        this.recipient = request.recipient();
        this.phone = request.phone();
        this.zonecode = request.zonecode();
        this.address = request.address();
        this.detailAddress = request.detailAddress();
        this.defaultYN = request.defaultYN();
    }
}

