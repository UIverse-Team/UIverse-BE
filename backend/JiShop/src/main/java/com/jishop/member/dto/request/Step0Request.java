package com.jishop.member.dto.request;

import jakarta.validation.constraints.AssertTrue;

public record Step0Request(
        @AssertTrue(message = "필수 사항입니다!!!")
        boolean ageAgreement,
        @AssertTrue(message = "필수 사항입니다!!!")
        boolean useAgreement,
        @AssertTrue(message = "필수 사항입니다!!!")
        boolean picAgreement,
        boolean adAgreement
) {
}
