package com.demo.weibo.microblog;

import com.demo.weibo.common.annotation.StartApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;

@StartApplication
@MapperScan("com.demo.weibo.microblog.mapper")
public class MicroblogApplication {

    public static void main(String[] args){
        SpringApplication.run(MicroblogApplication.class, args);
    }

}
