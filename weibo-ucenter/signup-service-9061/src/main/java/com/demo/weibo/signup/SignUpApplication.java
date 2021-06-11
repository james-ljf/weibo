package com.demo.weibo.signup;

import com.demo.weibo.common.annotation.StartApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@StartApplication
@MapperScan("com.demo.weibo.signup.mapper")
@EnableFeignClients("com.demo.weibo.api.client")
public class SignUpApplication {
    public static void main(String[] args) {
        SpringApplication.run(SignUpApplication.class, args);
    }
}
