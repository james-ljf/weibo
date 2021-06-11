package com.demo.weibo.microblog;

import com.demo.weibo.common.annotation.StartApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@StartApplication
@MapperScan("com.demo.weibo.microblog.mapper")
@EnableFeignClients("com.demo.weibo.api.client")
public class MicroblogApplication {

    public static void main(String[] args){
        SpringApplication.run(MicroblogApplication.class, args);
    }

}
