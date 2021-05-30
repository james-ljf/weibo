package com.demo.weibo.signup.controller;

import com.demo.weibo.common.annotation.UserLoginAnnotation;
import com.demo.weibo.common.util.R;
import com.demo.weibo.signup.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private AccountService accountService;

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
     * @param param 找回密码需要的信息(邮箱)
     * @return
     */
    @PostMapping("forget")
    public R forgetPassword(Map<String, String> param){
        return accountService.ForgetPassword(param);
    }

    /**
     * 验证 验证码是否正确
     * @param param
     * @return
     */
    @GetMapping("isVerify")
    public R isUserForgetVerify(Map<String, String> param){
        if (param.get("email") == null || param.get("verify") == null){
            return R.error("提交的信息不完全");
        }
        return accountService.isUserForgetVerify(param);
    }

}
