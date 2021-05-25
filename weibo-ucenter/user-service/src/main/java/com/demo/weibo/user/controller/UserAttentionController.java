package com.demo.weibo.user.controller;

import com.demo.weibo.common.util.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户关注操作
 */
@RestController
@RequestMapping("/user/attention")
public class UserAttentionController {

    @GetMapping
    public R addUserAttention(@RequestParam("uId") Long uId){
        return null;
    }

}
