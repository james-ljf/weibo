package com.demo.weibo.signup.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.demo.weibo.common.entity.User;
import com.demo.weibo.common.filter.RedissonBloomFilter;
import com.demo.weibo.common.util.R;
import com.demo.weibo.signup.mapper.UserMapper;
import com.demo.weibo.signup.service.AccountService;
import com.demo.weibo.signup.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoginService loginService;

    @Override
    public R ForgetPassword(Map<String, String> param) {
        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        wrapper.eq("account", param.get("account"));
        User user = new User();
        user.setPassword(passwordEncoder.encode(param.get("password")));
        int result = userMapper.update(user, wrapper);
        return result > 0 ? R.ok("重置密码成功") : R.error("重置失败");
    }

    @Override
    public R isUserForgetVerify(Map<String, String> param) {
        String email = param.get("email");
        String verify = param.get("verify");
        String redis_verify = stringRedisTemplate.opsForValue().get("userForgetVerify:" + email);
        if (redis_verify == null || redis_verify.equals("")){
            return R.error("验证码已过期，请重新发送");
        }
        if (!verify.equals(redis_verify)){
            return R.error("验证码不正确");
        }
        return R.ok("验证成功，进入下一步");
    }

    @Override
    public List<Long> selectAllId() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id");
        List<User> list = userMapper.selectList(queryWrapper);
        List<Long> idList = new ArrayList<>();
        for (User user : list) {
            idList.add(user.getId());
        }
        return idList;
    }

    @Override
    public List<String> selectAllAccount() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("account");
        List<User> list = userMapper.selectList(queryWrapper);
        List<String> accountList = new ArrayList<>();
        for (User user : list) {
            accountList.add(user.getAccount());
        }
        return accountList;
    }

    @Override
    public R updateUserPassword(Long uId, Map<String, Object> param) {
        User user = userMapper.selectById(uId);
        String oldPwd = (String) param.get("oldPwd");
        if(!passwordEncoder.matches(oldPwd, user.getPassword())){
            return R.error("旧密码输入错误，请重新输入。");
        }
        if (!param.get("newPwd1").equals(param.get("newPwd2"))){
            return R.error("新密码两次输入不一致。");
        }
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("u_id", uId);
        String newPwd = (String) param.get("newPwd1");
        user.setPassword(passwordEncoder.encode(newPwd));
        userMapper.update(user, updateWrapper);
        //修改密码成功，执行退出登录操作，需使用新密码重新登录
        loginService.loginOut(uId);
        return R.ok("修改成功");
    }

}
