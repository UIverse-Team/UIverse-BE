package com.jishop.member.dto.response;

public record BasicSocialUserInfo(
        String id,
        String name,
        String email
) implements SocialUserInfo {
}
