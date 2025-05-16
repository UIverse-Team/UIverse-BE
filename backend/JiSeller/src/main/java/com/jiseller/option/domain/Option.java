package com.jiseller.option.domain;

import com.jiseller.common.util.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "options")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Option extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OptionCategory categoryType;

    @Column(name = "option_value", nullable = false)
    private String optionValue;

    @Column(name = "option_extra", nullable = false, columnDefinition = "integer default 0")
    private int optionExtra;

    @Builder
    public Option(OptionCategory categoryType, String optionValue, int optionExtra) {
        this.categoryType = categoryType;
        this.optionValue = optionValue;
        this.optionExtra = optionExtra;
    }
}
