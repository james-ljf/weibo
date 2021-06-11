package com.demo.weibo.email.strategy.impl;

import com.demo.weibo.common.util.R;
import com.demo.weibo.email.configuration.EmailSender;
import com.demo.weibo.email.strategy.SendEmailStrategy;
import org.springframework.beans.factory.annotation.Autowired;

public class AttentionMqErrorStrategy implements SendEmailStrategy {

    @Autowired
    private EmailSender emailSender;

    @Override
    public boolean isMyStrategy(String strategy) {
        return "2".equals(strategy);
    }

    @Override
    public R callback(String to, String content) {
        emailSender.sendHtmlEmail(to, "关注服务出现异常", content, "MqErrorTemplate.html");
        return R.ok("发送成功");
    }
}
