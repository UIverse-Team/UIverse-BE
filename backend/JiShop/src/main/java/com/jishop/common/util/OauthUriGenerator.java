package com.jishop.common.util;

import com.jishop.property.AuthProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class OauthUriGenerator {

    private final AuthProperty authProperty;

    public String generateRedURL() {
        UriComponents uriComponents = UriComponentsBuilder
                .fromUriString(authProperty.getLoginUrl())
                .queryParam("response_type", "code")
                .queryParam("client_id", authProperty.getClientId())
                .queryParam("redirect_uri", authProperty.getRedirectUri())
                .queryParam("state", "STATE_STRING")
                .build();
        return uriComponents.toUriString();
    }
}
