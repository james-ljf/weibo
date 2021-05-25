package com.demo.weibo.signup.controller;

import com.demo.weibo.common.annotation.UserLoginAnnotation;
import com.demo.weibo.common.entity.User;
import com.demo.weibo.common.util.FormatUtil;
import com.demo.weibo.common.util.ObjectUtil;
import com.demo.weibo.common.util.R;
import com.demo.weibo.signup.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录
 */
@RestController
@RequestMapping("/sign/login-service")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @UserLoginAnnotation(needLogin = false)
    @PostMapping("/login")
    public Object login(HttpServletRequest request, @RequestBody User user){
        if (!ObjectUtil.checkFieldsNotEmptyIncludes(user, "account", "password") ||
                !FormatUtil.checkAccount(user.getAccount()) || !StringUtils.hasText(user.getPassword())){
            return R.error("您提交的参数有误！");
        }
        return loginService.Login(user);
    }

    //--用户登出
    @UserLoginAnnotation
    @PostMapping("/logout")
    public Object logout(HttpServletRequest request){
        User user = (User)request.getAttribute("weiboUser");
        loginService.loginOut(user.getId());
        return R.ok("已退出登录");
    }

}
