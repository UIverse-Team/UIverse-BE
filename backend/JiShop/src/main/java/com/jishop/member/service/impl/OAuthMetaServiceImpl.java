package com.jishop.member.service.impl;

import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.config.OAuthConfig;
import com.jishop.member.dto.response.OAuthMetaResponse;
import com.jishop.member.service.OAuthMetaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthMetaServiceImpl implements OAuthMetaService {

    private final OAuthConfig config;

    public OAuthMetaResponse getMeta(String provider) {
        System.out.println(">>> provider: [" + provider + "]");
        return switch (provider.toLowerCase()) {
            case "google" -> {
                var details = config.getGoogleDetails();
                yield new OAuthMetaResponse(details.getAuthorizationUri(), details.getClientId(), details.getRedirectUri());
            }
            case "naver" -> {
                var details = config.getNaverDetails();
                yield new OAuthMetaResponse(details.getAuthorizationUri(), details.getClientId(), details.getRedirectUri());
            }
            case "kakao" -> {
                var details = config.getKakaoDetails();
                yield new OAuthMetaResponse(details.getAuthorizationUri(), details.getClientId(), details.getRedirectUri());
            }
            default -> throw new DomainException(ErrorType.PROVIDER_NOT_FOUND);
        };
    }
}
