package com.demo.weibo.chat;

import com.demo.weibo.common.annotation.StartApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;

@StartApplication
@MapperScan("com.demo.weibo.chat.mapper")
public class ChatApplication {
    public static void main(String[] args){
        SpringApplication.run(ChatApplication.class, args);
    }
}
