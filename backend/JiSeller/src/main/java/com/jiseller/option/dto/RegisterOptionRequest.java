package com.jiseller.option.dto;

import com.jiseller.common.exception.DomainException;
import com.jiseller.common.exception.ErrorType;
import com.jiseller.option.domain.Option;
import com.jiseller.option.domain.OptionCategory;
import jakarta.validation.constraints.*;

public record RegisterOptionRequest(
        @NotNull(message = "카테고리 타입은 필수입니다")
        OptionCategory categoryType,

        @NotBlank(message = "옵션 값은 필수입니다")
        @Pattern(regexp = "^[가-힣a-zA-Z0-9\\s/]*$", message = "특수문자는 사용할 수 없습니다(슬래시(/)와 공백 제외)")
        String optionValue,

        @NotNull(message = "옵션 추가 가격은 필수입니다")
        @Min(value = 0, message = "옵션 추가 가격은 0원 이상이어야 합니다")
        @Max(value = 99999999, message = "옵션 추가 가격은 99,999,999원 이하여야 합니다")
        int optionExtra
) {
    public Option toEntity() {
        return Option.builder()
                .categoryType(this.categoryType)
                .optionValue(this.optionValue)
                .optionExtra(this.optionExtra)
                .build();
    }

    public void validateFashionClothesOptionFormat() {
        if (this.categoryType() == OptionCategory.FASHION_CLOTHES) {
            if (!this.optionValue().matches("^[가-힣a-zA-Z0-9]+/[가-힣a-zA-Z0-9]+$")) {
                throw new DomainException(ErrorType.VALIDATION_ERROR);
            }
        }
    }
}
