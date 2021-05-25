package com.demo.weibo.email.service.impl;


import com.demo.weibo.common.util.R;
import com.demo.weibo.email.service.EmailService;
import com.demo.weibo.email.strategy.SendEmailStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    List<SendEmailStrategy> strategies;

    @Override
    public R sendEmail(String to, String strategy, String content) {
        //判断这个Email是不是被注册了
        for (SendEmailStrategy sendEmailStrategy : strategies) {
            if (sendEmailStrategy.isMyStrategy(strategy)){
                return sendEmailStrategy.callback(to, content);
            }
        }
        return R.error("无效的请求！");
    }

}
