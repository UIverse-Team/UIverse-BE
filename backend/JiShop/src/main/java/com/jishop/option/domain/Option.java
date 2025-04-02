package com.jishop.option.domain;

import com.jishop.common.util.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "options")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Option extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private OptionCategory categoryType;

    @Column(name = "option_value", nullable = false)
    private String optionValue;

    @Column(name = "option_extra", nullable = false, columnDefinition = "integer default 0")
    private int optionExtra;

    public Option(OptionCategory categoryType, String optionValue, int optionExtra
    ) {
        this.categoryType = categoryType;
        this.optionValue = optionValue;
        this.optionExtra = optionExtra;
    }
}

