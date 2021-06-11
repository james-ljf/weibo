package com.demo.weibo.email.service;


import com.demo.weibo.common.util.R;

public interface EmailService {

    R sendEmail(String to, String strategy, String content);

}
