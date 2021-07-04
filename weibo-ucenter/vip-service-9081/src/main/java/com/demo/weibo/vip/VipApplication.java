package com.demo.weibo.vip;

import com.demo.weibo.common.annotation.StartApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@StartApplication
@MapperScan("com.demo.weibo.vip.mapper")
@EnableFeignClients("com.demo.weibo.api.client")
public class VipApplication {
    public static void main(String[] args){
        SpringApplication.run(VipApplication.class, args);
    }
}
