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
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserDetailMapper userDetailMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

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
        List<UserDetail> userDetailList = redisTemplate.opsForList().range("userDetailList:" + uId, 0, -1);
        //查缓存
        if (userDetailList != null && userDetailList.size() > 0){
            return R.ok("获取用户信息成功").addData("userDetailList", userDetailList);
        }
        //缓存没有，查数据库
        Map<String, Object> map = new HashMap<>();
        map.put("u_id", uId);
        userDetailList = userDetailMapper.selectByMap(map);
        if (userDetailList == null){
            return R.error("获取信息失败");
        }
        redisTemplate.opsForList().leftPushAll("userDetailList:" + uId, userDetailList);
        //stringRedisTemplate.opsForValue().set("userEmail:" + uId, userDetail.getEmail());
        //将email中间部分用*代替
        return R.ok("获取用户信息成功").addData("userDetailList", userDetailList);
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
