package com.jiseller.option.domain;

import com.jiseller.common.util.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Option extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OptionCategory categoryType;

    @Column(name = "option_value", nullable = false)
    private String optionValue;

    @Column(name = "option_extra", nullable = false, columnDefinition = "integer default 0")
    private int optionExtra;
}
