package com.demo.weibo.comment;

import com.demo.weibo.common.annotation.StartApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@StartApplication
@EnableFeignClients("com.demo.weibo.api.client")
public class CommentApplication {

    public static void main(String[] args){
        SpringApplication.run(CommentApplication.class, args);
    }

}
