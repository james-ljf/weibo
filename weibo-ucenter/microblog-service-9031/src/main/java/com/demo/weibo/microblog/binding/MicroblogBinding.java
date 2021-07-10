package com.demo.weibo.microblog.binding;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;


public interface MicroblogBinding {

    /**
     * 与配置文件的bindings的接收、发送消息binding名字配置一致
     */
    String OUTPUT = "setLike";

    String INPUT = "getLike";

    /**
     * 接收消息
     * @return null
     */
    @Input(MicroblogBinding.INPUT)
    SubscribableChannel getLike();

    /**
     * 发送消息
     * @return null
     */
    @Output(MicroblogBinding.OUTPUT)
    MessageChannel setLike();

}
