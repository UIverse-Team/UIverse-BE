package com.jishop.store.domain;

import com.jishop.common.util.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "stores")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends BaseEntity {

    @Column(name = "mall_seq", nullable = false, unique = true)
    private String mallSeq;

    @Column(name = "mall_name", nullable = false)
    private String mallName;

    public Store(String mallSeq, String mallName) {
        this.mallSeq = mallSeq;
        this.mallName = mallName;
    }
}