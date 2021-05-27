package com.demo.weibo.user.controller;

import com.demo.weibo.common.annotation.UserLoginAnnotation;
import com.demo.weibo.common.entity.User;
import com.demo.weibo.common.util.R;
import com.demo.weibo.user.service.UserAttentionService;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户关注操作
 */
@RestController
@RequestMapping("/user/attention")
public class UserAttentionController {

    @Autowired
    private UserAttentionService userAttentionService;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 添加用户关注
     * @param request
     * @param uId
     * @return
     */
    @GetMapping("/add")
    //@UserLoginAnnotation
    public R addUserAttention(HttpServletRequest request,Long mId, Long uId){
        User user = (User) request.getAttribute("weiboUser");
        Message<String> message = new Message<String>() {
            //{"uId" : "1"}格式
            @Override
            public String getPayload() {
                return "{\" "+ uId +" \" : \"1\"}";
            }

            @Override
            public MessageHeaders getHeaders() {
                return null;
            }
        };
        rocketMQTemplate.asyncSend("attention", message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("结果：" + sendResult);
            }

            @Override
            public void onException(Throwable throwable) {

            }
        });
        return R.ok("关注成功");
    }

}
