package com.jishop.member.dto.response;

import java.util.List;

public record OAuthMetaResponse(
        String authorizationUri,
        String clientId,
        String redirectUri
) {}
