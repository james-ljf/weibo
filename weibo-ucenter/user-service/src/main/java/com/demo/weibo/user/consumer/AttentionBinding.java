package com.demo.weibo.user.consumer;


import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

import java.util.Map;

public interface AttentionBinding {

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
