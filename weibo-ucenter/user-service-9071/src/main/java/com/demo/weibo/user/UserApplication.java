package com.demo.weibo.user;

import com.demo.weibo.common.annotation.StartApplication;
import com.demo.weibo.user.binding.AttentionBinding;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;

@StartApplication
@EnableFeignClients("com.demo.weibo.api.client")
@EnableBinding({AttentionBinding.class})
@MapperScan("com.demo.weibo.user.mapper")
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}