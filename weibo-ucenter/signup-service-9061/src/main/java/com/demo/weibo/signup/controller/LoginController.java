package com.demo.weibo.signup.controller;

import com.demo.weibo.common.annotation.UserLoginAnnotation;
import com.demo.weibo.common.entity.User;
import com.demo.weibo.common.util.FormatUtil;
import com.demo.weibo.common.util.ObjectUtil;
import com.demo.weibo.common.util.R;
import com.demo.weibo.signup.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录
 */
@RestController
@RequestMapping("/sign/login-service")
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * 用户登录
     * @param request
     * @param user
     * @return
     */
    @UserLoginAnnotation(needLogin = false)
    @PostMapping("/login")
    public R login(HttpServletRequest request, @RequestBody User user){
        if (!ObjectUtil.checkFieldsNotEmptyIncludes(user, "account", "password") ||
                !FormatUtil.checkAccount(user.getAccount()) || !StringUtils.hasText(user.getPassword())){
            return R.error("您提交的参数有误！");
        }
        return loginService.Login(user);
    }

    /**
     * 用户登出
     * @param request
     * @return
     */
    @UserLoginAnnotation
    @PostMapping("/logout")
    public Object logout(HttpServletRequest request){
        User user = (User)request.getAttribute("weiboUser");
        loginService.loginOut(user.getId());
        return R.ok("已退出登录");
    }

    /**
     * 判断本次登录的设备和设备ip是否为同一个
     * @return
     */
    @PostMapping("/security")
    public R isSecurityWorker(@RequestBody String account){
        return loginService.isWorkIpTrue(account);
    }

}
