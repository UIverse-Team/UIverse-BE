package com.jishop.member.service;

public interface OAuthProfile {

    String getProviderId();
    String getEmail();
    String getName();
    String getProvider();
}
