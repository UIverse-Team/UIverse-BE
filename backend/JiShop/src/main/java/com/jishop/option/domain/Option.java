package com.jishop.option.domain;


import com.jishop.common.util.BaseEntity;
import com.jishop.option.domain.embed.CategoryType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "options")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Option extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;

    private String optionValue;

    private int optionExtra;
}
