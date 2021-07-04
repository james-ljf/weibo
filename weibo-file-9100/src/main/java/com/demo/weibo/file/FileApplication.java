package com.demo.weibo.file;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

//防止服务在启动时加载连库信息
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan("com.demo.weibo")
public class FileApplication {
    public static void main(String[] args){
        SpringApplication.run(FileApplication.class, args);
    }

}
