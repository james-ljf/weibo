package com.demo.weibo.signup.service;

import com.demo.weibo.common.entity.User;
import com.demo.weibo.common.util.R;

public interface RegisterService {

    boolean accountExist(String account);

    boolean emailExist(String email);

    R registerUser(User user, String email, String email_code);

}
