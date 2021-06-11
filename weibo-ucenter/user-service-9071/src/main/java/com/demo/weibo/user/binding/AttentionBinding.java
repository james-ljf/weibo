package com.demo.weibo.user.binding;


import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * 自定义rocketmq的binding
 */
public interface AttentionBinding {

    /**
     * 与配置文件的bindings的接收、发送消息binding名字配置一致
     */
    String OUTPUT = "setAttention";

    String INPUT = "getAttention";

    /**
     * 接收消息
     * @return
     */
    @Input(AttentionBinding.INPUT)
    SubscribableChannel getAttention();

    /**
     * 发送消息
     * @return
     */
    @Output(AttentionBinding.OUTPUT)
    MessageChannel setAttention();

}
