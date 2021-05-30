package com.demo.weibo.user.controller;

import com.demo.weibo.common.entity.User;
import com.demo.weibo.common.util.R;
import com.demo.weibo.user.consumer.AttentionBinding;
import com.demo.weibo.user.service.UserAttentionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户关注操作
 */
@RestController
@RequestMapping("/user/attention")
@EnableBinding(AttentionBinding.class)
public class UserAttentionController {

    @Autowired
    private UserAttentionService userAttentionService;

    @Autowired
    private AttentionBinding attention;

    /**
     * 添加用户关注
     * @param request
     * @param uid
     * @return
     */
    @GetMapping("/add")
    //@UserLoginAnnotation
    public R addUserAttention(HttpServletRequest request,@RequestParam("mid") Long mid,@RequestParam("uid") Long uid){
        User user = (User) request.getAttribute("weiboUser");
        Map<String, Object> param = new HashMap();
        param.put("mid", mid);
        param.put("uid", uid);
        param.put("code", "1");
        attention.setAttention().send(MessageBuilder.withPayload(param).build());


//        this.source.output().send(
//                MessageBuilder.withPayload(param).build()
//        );


        return R.ok("关注成功");
    }

    @GetMapping("/cancel")
    public R cancelUserAttention(HttpServletRequest request, @RequestParam("uid") Long uid){
        return null;
    }

}
