package com.demo.weibo.util;

import com.demo.weibo.common.annotation.StartApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@StartApplication
@EnableFeignClients("com.demo.weibo.api.client")
public class UtilApplication {

    public static void main(String[] args){
        SpringApplication.run(UtilApplication.class, args);
    }

}
