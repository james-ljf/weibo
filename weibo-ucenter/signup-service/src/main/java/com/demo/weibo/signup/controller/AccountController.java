package com.demo.weibo.signup.controller;

import com.demo.weibo.common.annotation.UserLoginAnnotation;
import com.demo.weibo.common.util.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 *  注销账号、找回密码
 */
@RestController
@RequestMapping("/sign/account")
public class AccountController {

    /**
     * 注销账号
     * @param request
     * @return
     */
    @GetMapping("logoff")
    @UserLoginAnnotation
    public R logoffAccount(HttpServletRequest request){
        request.getAttribute("weiboUser");
        return null;
    }

    /**
     * 忘记密码
     * @param param 找回密码需要的信息
     * @return
     */
    @PostMapping("forget")
    public R forgetPassword(Map<String, Object> param){
        return null;
    }

}
