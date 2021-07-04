package com.demo.weibo.chat.service.impl;

import com.demo.weibo.chat.service.UserChatService;
import com.demo.weibo.chat.util.UserFriendComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserChatServiceImpl implements UserChatService {

    @Autowired
    private UserFriendComponent userFriendComponent;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;



}
