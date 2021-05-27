package com.demo.weibo.user.consumer;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * 消费者 拉模式 获取消息
 */
@Component
@RocketMQMessageListener(
        consumerGroup = "ATTENTION-GROUP",
        topic = "attention"
)
public class UserConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String s) {
        System.out.println(s);
    }
}
