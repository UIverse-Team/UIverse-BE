package com.jishop.member.controller.impl;

import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.config.OAuthConfig;
import com.jishop.member.controller.OAuthController;
import com.jishop.member.domain.User;
import com.jishop.member.dto.request.OAuthCallbackRequest;
import com.jishop.member.dto.response.OAuthMetaResponse;
import com.jishop.member.dto.response.TokenResponse;
import com.jishop.member.service.OAuthClient;
import com.jishop.member.service.OAuthMetaService;
import com.jishop.member.service.OAuthProfile;
import com.jishop.member.service.UserService;
import com.jishop.member.service.impl.GoogleClient;
import com.jishop.member.service.impl.KakaoClient;
import com.jishop.member.service.impl.NaverClient;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/oauth")
public class OAuthControllerImpl implements OAuthController {

    private final Map<String, OAuthClient> clients;
    private final UserService userService;
    private final OAuthMetaService metaService;

    @Autowired
    public OAuthControllerImpl(OAuthConfig config, RestClient restClient, UserService userService,
                               OAuthMetaService metaService) {
        this.userService = userService;
        this.metaService = metaService;
        clients = Map.of(
                "google", new GoogleClient(restClient, config.getGoogleDetails()),
                "naver", new NaverClient(restClient, config.getNaverDetails()),
                "kakao", new KakaoClient(restClient, config.getKakaoDetails())
        );
    }

    @GetMapping("/{provider}")
    public ResponseEntity<OAuthMetaResponse> authorizationUrl(@PathVariable String provider){

        return ResponseEntity.ok(metaService.getMeta(provider));
    }

    @PostMapping("/login")
    public ResponseEntity<OAuthProfile> login(@RequestBody OAuthCallbackRequest request,
                                              HttpSession session) {
        if (!clients.containsKey(request.provider())) {
            throw new DomainException(ErrorType.PROVIDER_NOT_FOUND);
        }

        if (request.code() == null || request.code().isEmpty()) {
            throw new DomainException(ErrorType.AUTHORIZATION_CODE_NOT_FOUND);
        }

        OAuthClient client = clients.get(request.provider());
        TokenResponse tokenResponse = client.getAccessToken(request.code());
        OAuthProfile profile = client.getProfile(tokenResponse.accessToken());

        User user = userService.oauthLogin(profile);
        session.setAttribute("userId", user.getId());
        session.setMaxInactiveInterval(60 * 30);


        return ResponseEntity.ok(profile);
    }
}