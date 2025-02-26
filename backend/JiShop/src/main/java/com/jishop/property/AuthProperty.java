package com.jishop.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "oauth2.naver")
public class AuthProperty {
    // 설정값들 세팅
    private String loginUrl;
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String tokenUri;
    private String refreshUri;
}
