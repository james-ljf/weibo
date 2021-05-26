package com.demo.weibo.user.controller;

import com.demo.weibo.common.annotation.UserLoginAnnotation;
import com.demo.weibo.common.entity.User;
import com.demo.weibo.common.util.R;
import com.demo.weibo.user.service.UserAttentionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/add")
    @UserLoginAnnotation
    public R addUserAttention(HttpServletRequest request, Long uId){
        User user = (User) request.getAttribute("weiboUser");
        return userAttentionService.addAttention(user.getId(), uId);
    }

}
