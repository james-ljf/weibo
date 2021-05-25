package com.demo.weibo.signup.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.demo.weibo.common.entity.User;
import com.demo.weibo.common.entity.UserState;
import com.demo.weibo.common.util.JwtUtil;
import com.demo.weibo.common.util.R;
import com.demo.weibo.common.util.ip.IpUtils;
import com.demo.weibo.signup.mapper.UserMapper;
import com.demo.weibo.signup.mapper.UserStateMapper;
import com.demo.weibo.signup.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserStateMapper userStateMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R Login(User user) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("account", user.getAccount());
        //查询数据库
        User loginUser = userMapper.selectOne(wrapper);
        //判断该用户是否存在
        if (loginUser == null){
            return R.error("该账号不存在，请重新输入");
        }
        //判断密码是否正确
        if (!passwordEncoder.matches(user.getPassword(), loginUser.getPassword())){
            return R.error("密码错误");
        }
        //存入Redis
        redisTemplate.opsForValue().set("loginUser:" + loginUser.getId(), loginUser, 1, TimeUnit.HOURS);
        //生成Token
        Map<String, Object> data = new HashMap<>();
        data.put("id", loginUser.getId());
        String token = JwtUtil.generateToken(data);
        //更新登录状态
        UpdateWrapper<UserState> updateWrapper = new UpdateWrapper();
        updateWrapper.eq("u_id", loginUser.getId());
        UserState userState = new UserState();
        userState.setLoginTime(new Date())
                .setUCode(1)
                .setIp(IpUtils.getHostIp())
                .setHostName(IpUtils.getHostName());
        int result = userStateMapper.update(userState, updateWrapper);
        return result > 0 ? R.ok("登录成功").addData("token", token) : R.error("网络繁忙");
    }

    @Override
    public void loginOut(Long id) {
        redisTemplate.delete("loginUser:" + id);
        //设置登录状态
        UserState userState = new UserState();
        userState.setURemember(0).setUCode(0);
        //以用户id查询并修改数据库中的登录状态
        UpdateWrapper<UserState> updateWrapper = new UpdateWrapper();
        updateWrapper.eq("u_id", id);
        userStateMapper.update(userState, updateWrapper);
    }

}
