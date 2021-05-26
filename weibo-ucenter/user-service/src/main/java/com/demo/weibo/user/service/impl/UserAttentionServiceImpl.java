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
    @SuppressWarnings("all")
    public R addAttention(Long mId, Long uId) {
        Map param = redisTemplate.opsForHash().entries("userAttention:" + mId);
        if (param != null && !param.isEmpty()){
            Set set = param.entrySet();
            Iterator iterator = set.iterator();
            while (iterator.hasNext()){ //使用迭代器遍历寻找map的key
                Map.Entry map = (Map.Entry) iterator.next();
                if (map.getKey() == uId){  //如果key值和uid的值相等
                    return R.error("已关注该用户，请不要重复关注。");
                }
            }
        }

        param.put(uId.toString(), "1");
        redisTemplate.opsForHash().putAll("userAttention:" + mId, param);
        return R.ok("关注成功");
    }

}
