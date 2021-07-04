package com.demo.weibo.pay;

import com.demo.weibo.common.annotation.StartApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@StartApplication
@MapperScan("com.demo.weibo.pay.mapper")
@EnableFeignClients("com.demo.weibo.api.client")
public class PayApplication {
    public static void main(String[] args){
        SpringApplication.run(PayApplication.class, args);
    }
}
