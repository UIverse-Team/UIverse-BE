package com.jishop.store.domain;

import com.jishop.common.util.BaseEntity;
import com.jishop.product.domain.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "stores")
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

    // 해당 스토어가 가진 상품 리스트 (양방향 관계)
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();

    @Builder
    public Store(String mallSeq, String mallId, String mallName, String mobileUrl, String pcUrl, Boolean isBrandStore) {
        this.mallSeq = mallSeq;
        this.mallId = mallId;
        this.mallName = mallName;
        this.mobileUrl = mobileUrl;
        this.pcUrl = pcUrl;
        this.isBrandStore = isBrandStore;
    }

    // 상품 추가 메소드
    public void addProduct(Product product) {
        products.add(product);
    }
}
