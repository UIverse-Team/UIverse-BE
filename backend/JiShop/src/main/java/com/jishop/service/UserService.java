package com.jishop.service;

import com.jishop.domain.LoginType;
import com.jishop.domain.User;
import com.jishop.dto.SocialUserInfo;

public interface UserService {

    User processOAuthUser(SocialUserInfo socialUserInfo, LoginType provider);
}
