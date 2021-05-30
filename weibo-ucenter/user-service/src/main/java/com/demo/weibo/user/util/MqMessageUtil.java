package com.demo.weibo.user.util;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

public class MqMessageUtil {

    public static <T> Message<T> getMessage(T object){
        Message<T> message = new Message<T>() {
            @Override
            public T getPayload() {
                return object;
            }

            @Override
            public MessageHeaders getHeaders() {
                return null;
            }
        };
        return message;
    }

}
