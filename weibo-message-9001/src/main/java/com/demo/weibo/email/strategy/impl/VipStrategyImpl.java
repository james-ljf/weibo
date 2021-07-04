package com.demo.weibo.email.strategy.impl;

import com.demo.weibo.common.util.R;
import com.demo.weibo.email.configuration.EmailSender;
import com.demo.weibo.email.strategy.SendEmailStrategy;
import org.springframework.beans.factory.annotation.Autowired;

public class VipStrategyImpl implements SendEmailStrategy {

    @Autowired
    private EmailSender emailSender;

    @Override
    public boolean isMyStrategy(String strategy) {
        return "3".equals(strategy);
    }

    @Override
    public R callback(String to, String content) {
        System.out.println("我发会员邮件");
        emailSender.sendHtmlEmail(to, "旧浪微博充值会员结果", content, "VipTemplate.html");
        return R.ok("发送成功");
    }

}
