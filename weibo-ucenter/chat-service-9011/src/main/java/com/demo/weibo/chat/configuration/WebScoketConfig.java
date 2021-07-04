package com.demo.weibo.chat.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebScoketConfig{
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/weibo-chat").setAllowedOrigins("*").withSockJS();
//    }
//
//    //配置消息代理Message Broker
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        //点对点配置/user消息代理
//        registry.enableSimpleBroker("/topic","/user");
//        //配置订阅前缀
//        registry.setUserDestinationPrefix("/user");
//    }


}
