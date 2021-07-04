package com.demo.weibo.util.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduledTasks {

    /**
     * 将微博、用户信息从redis持久化到mysql
     */
    @Scheduled(cron = "0 0 1 * * ?" ) //每天凌晨一点执行
    public void renew(){

    }

}
