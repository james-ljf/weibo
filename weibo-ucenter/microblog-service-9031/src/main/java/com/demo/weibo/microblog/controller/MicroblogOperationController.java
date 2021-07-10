package com.demo.weibo.microblog.controller;

import com.demo.weibo.common.annotation.UserLoginAnnotation;
import com.demo.weibo.common.entity.User;
import com.demo.weibo.common.entity.mq.MicroblogMQ;
import com.demo.weibo.common.util.R;
import com.demo.weibo.microblog.binding.MicroblogBinding;
import com.demo.weibo.microblog.service.MicroblogOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/operation")
@EnableBinding(MicroblogBinding.class)
public class MicroblogOperationController {

    @Autowired
    private MicroblogOperationService microblogOperationService;

    @Autowired
    private MicroblogBinding microblogBinding;

    /**
     * 点赞微博
     * @param request   获取当前用户
     * @param cId   微博id
     * @return  R
     */
    @GetMapping("/like")
    @UserLoginAnnotation
    public R addLikeMicroblog(HttpServletRequest request,
                              @RequestParam("cid") Long cId){
        User user = (User) request.getAttribute("weiboUser");
        if (user == null){
            return R.error("请先进行登录");
        }
        MicroblogMQ microblogMQ = new MicroblogMQ();
        microblogMQ.setCId(cId).setUId(user.getId()).setStrategy(1);
        microblogBinding.setLike().send(MessageBuilder.withPayload(microblogMQ).build());
        return R.ok("点赞成功");
    }

    /**
     * 取消点赞微博
     * @param request   获取当前用户
     * @param cId   微博id
     * @return  R
     */
    @GetMapping("/unlike")
    @UserLoginAnnotation
    public R cancelLikeMicroblog(HttpServletRequest request,  @RequestParam("cid") Long cId){
        User user = (User) request.getAttribute("weiboUser");
        if (user == null){
            return R.error("请先进行登录");
        }
        MicroblogMQ microblogMQ = new MicroblogMQ();
        microblogMQ.setCId(cId).setUId(user.getId()).setStrategy(2);
        microblogBinding.setLike().send(MessageBuilder.withPayload(microblogMQ).build());
        return R.error("取消点赞成功");
    }

    /**
     * 查看用户是否点赞了该微博（服务调用）
     * @param cId   微博id
     * @param uId   用户id
     * @return  boolean
     */
    @PostMapping("/is-like")
    public boolean isLikeMicroblog(@RequestParam("cId") Long cId, @RequestParam("uId") Long uId){
        return microblogOperationService.isLikeMicroblog(cId, uId);
    }

}
