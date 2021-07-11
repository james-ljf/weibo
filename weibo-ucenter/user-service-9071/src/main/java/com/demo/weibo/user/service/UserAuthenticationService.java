package com.demo.weibo.user.service;

import com.demo.weibo.common.entity.UserAuthentication;
import com.demo.weibo.common.util.R;

public interface UserAuthenticationService {

    R addAuthentication(UserAuthentication userAuthentication);

    R selectAuthentication(Long uId);

    R selectAuthenticationById(Long uId);

}
