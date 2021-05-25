package com.demo.weibo.common.util;

import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

public class RedisLockUtil {

    public static Boolean lock(StringRedisTemplate stringRedisTemplate, String lockName){
        return stringRedisTemplate.opsForValue().setIfAbsent("redisLock:" + lockName, "lock", 1500, TimeUnit.MILLISECONDS);
    }

    public static void unlock(StringRedisTemplate stringRedisTemplate, String lockName){
        stringRedisTemplate.delete("redisLock:" + lockName);
    }

}
