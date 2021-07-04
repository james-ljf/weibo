package com.demo.weibo.chat.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//将目前的类定义成一个webscoket服务器端
@ServerEndpoint("/weibo-chat/{uId}")
@Component
@Slf4j
public class ChatService {

    /**
     * String  用户id
     * Session 发送/回话
     */
    public static Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    /**
     * 连接成功
     * @param session
     */
    @OnOpen
    public void Open(Session session, @PathParam("uId") String uId){
        //如果存放连接用户的map为空
        if (sessionMap == null){
            sessionMap = new ConcurrentHashMap<>();
        }
        sessionMap.put(uId, session);
        log.info("连接成功----" + uId);
    }

    /**
     * 关闭连接
     */
    @OnClose
    public void Close(@PathParam("uId") String uId){
        log.info("连接断开----" + this);
        sessionMap.remove(uId);
    }

    /**
     * 接收客户端的消息
     * @param msgMap
     */
    @OnMessage
    public void OnMessage(String msgMap, Session session){
        log.info("接收到客户端的消息：" + msgMap + ", 客户端id是:" + session.getId());
        sendMessageTo(msgMap);
    }

    /**
     * 一对一聊天发消息
     * @param msgMap
     */
    public void sendMessageTo(String msgMap){
        System.out.println("未转化json："+msgMap);
        JSONObject jsonObject = JSON.parseObject(msgMap);
        String uId = jsonObject.getString("uId");
        Session userSession = sessionMap.get(uId);
        if (userSession == null){
            log.info("用户不在线");
        }else {
            System.out.println("发送的消息----" + msgMap);
            userSession.getAsyncRemote().sendObject(msgMap);
        }
    }

}
