package com.demo.weibo.user.service;

import com.demo.weibo.common.entity.UserDetail;
import com.demo.weibo.common.util.R;

import java.util.List;

public interface UserInfoService {

    Integer addUserInfo(UserDetail userDetail);

    UserDetail selectUserEmail(String email);

    R selectUserAll(Long uId);

    R updateUserInfo(UserDetail userDetail);

}
