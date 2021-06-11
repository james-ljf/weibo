package com.demo.weibo.user.controller;

import com.demo.weibo.common.entity.UserAuthentication;
import com.demo.weibo.common.util.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户认证操作
 */
@RestController
@RequestMapping("/user/authentication")
public class UserAuthenticationController {

    @GetMapping("/add")
    public R addAuthentication(@RequestBody UserAuthentication userAuthentication){
        return null;
    }

}
