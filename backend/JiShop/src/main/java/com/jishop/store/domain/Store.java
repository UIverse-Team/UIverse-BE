package com.jishop.store.domain;

import com.jishop.common.util.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "store")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends BaseEntity {

    @Column(name = "mall_seq", nullable = false, unique = true)
    private String mallSeq;

    @Column(name = "mall_id")
    private String mallId;

    @Column(name = "mall_name", nullable = false)
    private String mallName;

    @Column(name = "mobile_url")
    private String mobileUrl;

    @Column(name = "pc_url")
    private String pcUrl;

    @Column(name = "is_brand_store")
    private Boolean isBrandStore;

    @Builder
    public Store(String mallSeq, String mallId, String mallName, String mobileUrl, String pcUrl, Boolean isBrandStore) {
        this.mallSeq = mallSeq;
        this.mallId = mallId;
        this.mallName = mallName;
        this.mobileUrl = mobileUrl;
        this.pcUrl = pcUrl;
        this.isBrandStore = isBrandStore;
    }
}