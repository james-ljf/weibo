package com.demo.weibo.signup.service;

import com.demo.weibo.common.util.R;

import java.util.Map;

public interface AccountService {

    R ForgetPassword(Map<String, String> param);

    R isUserForgetVerify(Map<String, String> param);

}
