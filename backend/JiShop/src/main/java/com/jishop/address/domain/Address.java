package com.jishop.address.domain;

import com.jishop.address.dto.AddressResponse;
import com.jishop.common.util.BaseEntity;
import com.jishop.member.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name="addresses")
public class Address extends BaseEntity {

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name="zone_code")
    private String zonecode;

    private String address;

    @Column(name="detail_address")
    private String detailAddress;

    private boolean defaultYN;

    @Builder
    public Address(User user, String zonecode, String address, String detailAddress, boolean defaultYN) {
        this.user = user;
        this.zonecode = zonecode;
        this.address = address;
        this.detailAddress = detailAddress;
        this.defaultYN = defaultYN;
    }

    public void updateDefaultYN() {
        this.defaultYN = false;
    }


}

