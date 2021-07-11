package com.demo.weibo.user.controller;

import com.demo.weibo.common.annotation.UserLoginAnnotation;
import com.demo.weibo.common.entity.User;
import com.demo.weibo.common.entity.UserAuthentication;
import com.demo.weibo.common.util.R;
import com.demo.weibo.user.service.UserAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 用户认证操作
 */
@RestController
@RequestMapping("/user/authentication")
public class UserAuthenticationController {

    @Autowired
    private UserAuthenticationService userAuthenticationService;

    /**
     * 添加认证
     * @param request   获取当前用户
     * @param aContent  认证内容
     * @param aReason   认证原因
     * @return  R
     */
    @GetMapping("/add")
    @UserLoginAnnotation
    public R addAuthentication(HttpServletRequest request,
                               @RequestParam("aContent") String aContent,
                               @RequestParam("aReason") String aReason){
        UserAuthentication userAuthentication = new UserAuthentication();
        //设置实体类参数
        userAuthentication.setAContent(aContent).setAReason(aReason);

        User user = (User) request.getAttribute("weiboUser");
        userAuthentication.setUId(user.getId());

        return userAuthenticationService.addAuthentication(userAuthentication);
    }

    /**
     * 查询自己认证信息
     * @param request   获取当前用户
     * @return  R
     */
    @GetMapping("/select")
    @UserLoginAnnotation
    public R selectAuthentication(HttpServletRequest request){
        User user = (User) request.getAttribute("weiboUser");
        return userAuthenticationService.selectAuthentication(user.getId());
    }

    @GetMapping("/select/{uId}")
    public R selectAuthenticationById(@PathVariable Long uId){
        return userAuthenticationService.selectAuthenticationById(uId);
    }

}
