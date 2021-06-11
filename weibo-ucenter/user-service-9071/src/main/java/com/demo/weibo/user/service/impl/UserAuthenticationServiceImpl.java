package com.demo.weibo.user.service.impl;

import com.demo.weibo.common.entity.UserAuthentication;
import com.demo.weibo.common.util.R;
import com.demo.weibo.user.mapper.UserAuthenticationMapper;
import com.demo.weibo.user.service.UserAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

    @Autowired
    private UserAuthenticationMapper userAuthenticationMapper;

    @Override
    public R addAuthentication(UserAuthentication userAuthentication) {
        UserAuthentication userAuthentication_1 = userAuthenticationMapper.selectById(userAuthentication.getUId());
        if (userAuthentication_1 != null) {
            return R.error("您的认证正在审核中，请不要重复申请。");
        }
        userAuthentication.setAState(2).setATime(new Date());
        int result = userAuthenticationMapper.insert(userAuthentication);
        return result > 0 ? R.ok("申请认证成功") : R.error("申请认证失败，如有疑问可进行反馈");
    }

}
