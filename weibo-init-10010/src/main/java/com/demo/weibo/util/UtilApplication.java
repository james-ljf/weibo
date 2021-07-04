package com.demo.weibo.util;

import com.demo.weibo.common.annotation.StartApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;


@StartApplication
@EnableFeignClients("com.demo.weibo.api.client")
@EnableScheduling
public class UtilApplication {

    public static void main(String[] args){
        SpringApplication.run(UtilApplication.class, args);
    }

}
