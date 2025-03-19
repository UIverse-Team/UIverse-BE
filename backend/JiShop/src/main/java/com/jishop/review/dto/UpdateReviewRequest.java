package com.jishop.review.dto;

import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.review.domain.tag.Tag;
import jakarta.validation.constraints.NotBlank;

public record UpdateReviewRequest(

        @NotBlank(message = "리뷰 내용은 필요합니다.")
        String content,
        Tag tag,
        int rating
) {
    public UpdateReviewRequest {
        if (rating < 1 || rating > 5)
            throw new DomainException(ErrorType.RATING_OUT_OF_RANGE);
    }
}
