package com.demo.weibo.signup.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.demo.weibo.api.client.UserClient;
import com.demo.weibo.common.entity.User;
import com.demo.weibo.common.entity.UserDetail;
import com.demo.weibo.common.entity.UserState;
import com.demo.weibo.common.util.R;
import com.demo.weibo.common.util.id.IdGenerator;
import com.demo.weibo.common.util.ip.IpUtils;
import com.demo.weibo.signup.mapper.UserMapper;
import com.demo.weibo.signup.mapper.UserStateMapper;
import com.demo.weibo.signup.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserClient userClient;

    @Autowired
    private UserStateMapper userStateMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 账号是否已被注册
     * @param account
     * @return
     */
    @Override
    public boolean accountExist(String account) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        User user = userMapper.selectOne(queryWrapper.eq("account", account));
        return user != null;
    }

    /**
     * 邮箱是否已被注册
     * @param email
     * @return
     */
    @Override
    public boolean emailExist(String email) {
        UserDetail userDetail = userClient.selectUserEmail(email);
        return userDetail != null;
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R registerUser(User user, String email, String email_code) {
        //获取存在redis的验证码
        String redis_verify = stringRedisTemplate.opsForValue().get("userRegisterVerify:" + email);
        if (redis_verify == null || redis_verify == ""){
            return R.error("验证码已过期，请重新发送");
        }
        if (email_code == null || !redis_verify.equals(email_code)){
            return R.error("验证码不正确，请重新输入");
        }
        if (accountExist(user.getAccount())){
            return R.error("该账号已被注册");
        }
        if (emailExist(email)){
            return R.error("该邮箱已被注册");
        }
        Date date = new Date();
        user.setCreate_time(date).setState(1).setId(IdGenerator.snowflakeId());
        user.setPassword(passwordEncoder.encode(user.getPassword())); //密码加密
        int result = userMapper.insert(user);
        //存用户详细信息表
        UserDetail userDetail = new UserDetail();
        userDetail.setEmail(email).setUId(user.getId());
        Object r = userClient.addUserInfo(userDetail);
        //存用户登录状态表
        UserState userState = new UserState();
        userState.setUId(user.getId());
        int result1 = userStateMapper.insert(userState);
        return result > 0 && r != null && result1 > 0 ? R.ok("注册成功") : R.error("注册失败");
    }
}
