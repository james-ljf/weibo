package com.demo.weibo.file;


import com.demo.weibo.common.annotation.StartApplication;
import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@Import(FdfsClientConfig.class)
public class FileApplication {

    public static void main(String[] args){
        SpringApplication.run(FileApplication.class, args);
    }

}
