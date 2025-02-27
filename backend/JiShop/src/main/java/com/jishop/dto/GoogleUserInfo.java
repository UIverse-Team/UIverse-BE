package com.jishop.dto;

import lombok.Getter;

@Getter
public class GoogleUserInfo {

    private String id;
    private String email;
    private Boolean verifiedEmail;
    private String name;
    private String givenName;
    private String familyName;
    private String picture;
    private String locale;
}