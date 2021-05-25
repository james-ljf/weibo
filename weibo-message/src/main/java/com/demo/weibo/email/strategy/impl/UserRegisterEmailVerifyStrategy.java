package com.demo.weibo.email.strategy.impl;


import com.demo.weibo.common.util.R;
import com.demo.weibo.email.configuration.EmailSender;
import com.demo.weibo.email.strategy.SendEmailStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class UserRegisterEmailVerifyStrategy implements SendEmailStrategy {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private EmailSender emailSender;

    @Override
    public boolean isMyStrategy(String strategy) {
        return "0".equals(strategy);
    }

    @Override
    public R callback(String to, String content) {
        String redisVerify = stringRedisTemplate.opsForValue().get("userRegisterVerify:" + to);
        //判断已经发送的验证码是否失效
        if (redisVerify != null){
            //未失效
            return R.error("请不要频繁的发送验证码！");
        }
        //生成验证码
        //随机生成6位数的验证码
        int data = (int) ((Math.random() * 9 + 1) * 100000);

        String emailData = String.valueOf(data);
        System.out.println(emailData);
        //发送验证码
        emailSender.sendHtmlEmail(to, "微博注册", emailData, "VerifyTemplate.html");
        //存入redis
        stringRedisTemplate.opsForValue().set("userRegisterVerify:" + to, emailData, 180, TimeUnit.SECONDS);
        return R.ok("发送成功！");
    }

}

