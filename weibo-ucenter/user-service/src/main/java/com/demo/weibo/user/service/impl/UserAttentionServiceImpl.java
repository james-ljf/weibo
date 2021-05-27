package com.demo.weibo.user.service.impl;

import com.demo.weibo.common.util.R;
import com.demo.weibo.user.service.UserAttentionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserAttentionServiceImpl implements UserAttentionService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    //@SuppressWarnings("all")
    public R addAttention(Long mId, Long uId) {
//        Map<Object, Object> param = redisTemplate.opsForHash().entries("userAttention:" + mId);
//        if (!param.isEmpty()){ //判断 param 不为空且不为{}
//            Set<Map.Entry<Object, Object>> set = param.entrySet();
//            for (Map.Entry map : set) { //使用迭代器遍历寻找map的key
//                if (map.getKey() == uId.toString()) {  //如果key值和uid的值相等
//                    return R.error("已关注该用户，请不要重复关注。");
//                }
//            }
//        }
//        param.put(uId.toString(), "1");
//        redisTemplate.opsForHash().putAll("userAttention:" + mId, param);
//        return R.ok("关注成功");

        return null;
    }

}
