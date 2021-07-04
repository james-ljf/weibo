package com.demo.weibo.signup.service;

import com.demo.weibo.common.entity.User;
import com.demo.weibo.common.util.R;

public interface LoginService {

    R Login(User user);

    void loginOut(Long id);

    R isWorkIpTrue(String account);


}
