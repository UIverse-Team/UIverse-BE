package com.jiseller.store.domain;

import com.jishop.common.util.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends BaseEntity {

    @Column(name = "mall_seq", nullable = false, unique = true)
    private Long mallSeq;

    @Column(name = "mall_name", nullable = false)
    private String mallName;

    @Column(name = "wish_count", nullable = false, columnDefinition = "int default 0")
    private int wishCount;
}
