package com.demo.weibo.email.strategy.impl;

import com.demo.weibo.common.util.R;
import com.demo.weibo.email.configuration.EmailSender;
import com.demo.weibo.email.strategy.SendEmailStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 策略 1
 * 用户找回密码发送的邮件
 */
@Component
public class UserForgetPasswordStrategy implements SendEmailStrategy {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private EmailSender emailSender;

    @Override
    public boolean isMyStrategy(String strategy) {
        return "1".equals(strategy);
    }

    @Override
    public R callback(String to, String content) {
        String redisVerify = stringRedisTemplate.opsForValue().get("userForgetVerify:" + to);
        if (redisVerify != null) {
            //验证码未失效
            return R.error("请不要频繁发送验证码");
        }
        //随机生成6位数的验证码
        int data = (int) ((Math.random() * 9 + 1) * 100000);
        //将验证码强转为字符串类型
        String emailData = String.valueOf(data);
        emailSender.sendHtmlEmail(to, "密码重置代码", emailData, "ForgetTemplate.html");
        //存入redis
        stringRedisTemplate.opsForValue().set("userForgetVerify:" + to, emailData, 180, TimeUnit.SECONDS);
        return R.ok("发送成功！");
    }
}
