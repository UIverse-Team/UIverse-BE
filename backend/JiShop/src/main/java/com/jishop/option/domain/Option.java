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
    OptionCategory category_type;
    String optionValue;
    Integer optionExtra;
}
