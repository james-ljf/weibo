package com.demo.weibo.util.init;

import com.demo.weibo.api.client.UserClient;
import com.demo.weibo.common.entity.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RedisInit implements CommandLineRunner {

    @Autowired
    private UserClient userClient;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void run(String... args) throws Exception {

        //初始化用户信息，将其从数据库中存到缓存
        //初始化用户id，以List形式存到缓存
        List<UserDetail> userDetailList = userClient.selectAll();
        System.out.println("正在初始化用户信息");
        for (UserDetail userDetail : userDetailList) {
            redisTemplate.opsForValue().set("UserDetail:" + userDetail.getUId(), userDetail);
            redisTemplate.opsForList().leftPush("UserID", userDetail.getUId());
        }
        System.out.println("初始化用户信息成功");




    }

}
