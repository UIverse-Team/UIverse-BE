package com.jishop.member.dto.response;

public record NaverSocialUserInfo(
        String id,
        String name,
        String email,
        String gender,
        String mobile,
        String birthday
) implements SocialUserInfo {
}
