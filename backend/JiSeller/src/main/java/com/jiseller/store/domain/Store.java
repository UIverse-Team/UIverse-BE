package com.jiseller.store.domain;

import com.jiseller.common.util.BaseEntity;
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
    private Long mallSeq;

    @Column(name = "mall_name", nullable = false)
    private String mallName;

    @Column(name = "wish_count", nullable = false, columnDefinition = "int default 0")
    private int wishCount;
}
