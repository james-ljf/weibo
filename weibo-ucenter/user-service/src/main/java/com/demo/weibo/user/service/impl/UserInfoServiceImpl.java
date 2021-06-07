package com.demo.weibo.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.demo.weibo.common.entity.UserDetail;
import com.demo.weibo.common.util.R;
import com.demo.weibo.user.mapper.UserDetailMapper;
import com.demo.weibo.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
//@SuppressWarnings("all")
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserDetailMapper userDetailMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Integer addUserInfo(UserDetail userDetail) {
        return userDetailMapper.insert(userDetail);
    }

    @Override
    public UserDetail selectUserEmail(String email) {
        QueryWrapper<UserDetail> queryWrapper = new QueryWrapper<>();
        return userDetailMapper.selectOne(queryWrapper.eq("email", email));
    }

    @Override
    public R selectUserAll(Long uId) {
        //先查缓存有没有
        UserDetail userDetail = (UserDetail) redisTemplate.opsForValue().get("UserDetail:" + uId);
        if (userDetail != null){
            return R.ok("获取用户信息成功").addData("userDetail", userDetail);
        }
        //缓存没有，查数据库
        QueryWrapper<UserDetail> wrapper = new QueryWrapper<>();
        userDetail = userDetailMapper.selectOne(wrapper.eq("u_id", uId));
        if (userDetail == null){
            return R.error("获取信息失败");
        }
        redisTemplate.opsForValue().set("UserDetail:" + uId, userDetail);
        //stringRedisTemplate.opsForValue().set("userEmail:" + uId, userDetail.getEmail());
        //将email中间部分用*代替
        return R.ok("获取用户信息成功").addData("userDetail", userDetail);
    }

    @Override
    public R updateUserInfo(UserDetail userDetail) {
        UpdateWrapper<UserDetail> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("u_id", userDetail.getUId());
        //根据用户id更改用户信息
        int result = userDetailMapper.update(userDetail, updateWrapper);
        return result > 0 ? R.ok("更新成功") : R.error("更新失败，请稍后再试");
    }

}
