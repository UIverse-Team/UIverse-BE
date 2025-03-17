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

    @Column(nullable = false)
    private String optionValue;

    @Column(nullable = false, columnDefinition = "integer default 0")
    private int optionExtra;

    public Option(final OptionCategory categoryType, final String optionValue, final int optionExtra) {
        this.categoryType = categoryType;
        this.optionValue = optionValue;
        this.optionExtra = optionExtra;
    }
}

