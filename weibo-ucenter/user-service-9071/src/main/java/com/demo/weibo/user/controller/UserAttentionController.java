package com.demo.weibo.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.demo.weibo.common.annotation.UserLoginAnnotation;
import com.demo.weibo.common.entity.User;
import com.demo.weibo.common.entity.mq.Attention;
import com.demo.weibo.common.util.R;
import com.demo.weibo.user.binding.AttentionBinding;
import com.demo.weibo.user.service.UserAttentionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户关注操作
 */
@RestController
@RequestMapping("/user/attention")
@EnableBinding(AttentionBinding.class)
public class UserAttentionController {

    @Autowired
    private AttentionBinding attentionBinding;

    @Autowired
    private UserAttentionService attentionService;


    /**
     * 添加关注
     * @param request 获取当前用户id
     * @param u2Id 被关注的用户
     * @param strategy 1
     * @return R
     */
    @GetMapping("/add")
    @UserLoginAnnotation
    public R addUserAttention(HttpServletRequest request,
                              @RequestParam("u2id") Long u2Id,
                              @RequestParam("strategy") String strategy){

        User user = (User) request.getAttribute("weiboUser");

        //生产者提交给消费者的信息实体类
        Attention a = new Attention(user.getId(), u2Id, strategy);
        //将消息提交到消息队列
        attentionBinding.setAttention().send(MessageBuilder.withPayload(a).build());

        return R.ok("关注成功");
    }

    /**
     * 取消关注
     * @param request 获取当前用户id
     * @param u2Id 被关注用户id
     * @param strategy 2
     * @return R
     */
    @GetMapping("/cancel")
    @UserLoginAnnotation
    public R cancelUserAttention(HttpServletRequest request,
                                 @RequestParam("u2id") Long u2Id,
                                 @RequestParam("strategy") String strategy){
        User user = (User) request.getAttribute("weiboUser");

        //生产者提交给消费者的信息实体类
        Attention a = new Attention(user.getId(), u2Id, strategy);
        //将消息提交到消息队列
        attentionBinding.setAttention().send(MessageBuilder.withPayload(a).build());

        return R.ok("取消关注成功");
    }

    /**
     * 查询自己的所有关注(直接访问接口)
     * @param request 获取用户id
     * @return R
     */
    @GetMapping("/all")
    @UserLoginAnnotation
    public R findAllUserAttention(HttpServletRequest request){
        User user = (User) request.getAttribute("weiboUser");
        return attentionService.findAllUserAttention(user.getId());
    }

    /**
     * 查询自己的所有关注(供服务调用查询)
     * @param uId  自己的id
     * @return  List
     */
    @GetMapping("/myself-attention")
    public List<JSONObject> findAllMyAttention(@RequestParam("uId") Long uId){
        return attentionService.findAllMyAttention(uId);
    }

    /**
     * 查询自己或者某个用户的所有关注
     * @param uId 用户id
     * @return R
     */
    @GetMapping("/all-attention")
    @UserLoginAnnotation
    public R findOneUserAttention(HttpServletRequest request, @RequestParam("uId") Long uId){
        User user = (User) request.getAttribute("weiboUser");
        if (user == null){
            return attentionService.findAllUserAttention(uId);
        }
        return attentionService.findAllUserAttention(user.getId());
    }


    /**
     * 查询当前用户的好友列表（即互相关注的人）
     * @param request   获取当前用户
     * @return  R
     */
    @GetMapping("/friend")
    @UserLoginAnnotation
    public R findUserAllFriend(HttpServletRequest request){
        User user = (User) request.getAttribute("weiboUser");
        return attentionService.findAllUserFriend(user.getId());
    }

    /**
     * 查询当前用户的所有粉丝
     * @param request   获取当前用户
     * @return  R
     */
    @GetMapping("/all-fans")
    @UserLoginAnnotation
    public R findMyAllFans(HttpServletRequest request){
        User user = (User) request.getAttribute("weiboUser");
        return attentionService.findAllUserFans(user.getId());
    }

}
