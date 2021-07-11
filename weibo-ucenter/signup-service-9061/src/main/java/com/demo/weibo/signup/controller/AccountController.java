package com.demo.weibo.signup.controller;

import com.demo.weibo.common.annotation.UserLoginAnnotation;
import com.demo.weibo.common.entity.User;
import com.demo.weibo.common.util.R;
import com.demo.weibo.signup.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 *  注销账号、修改密码、找回密码、查询所有用户的id
 */
@RestController
@RequestMapping("/sign/account")
public class AccountController {

    @Autowired
    private AccountService accountService;


    /**
     * 注销账号(未完成)
     * @param request   获取用户
     * @return  String
     */
    @GetMapping("/logoff")
    @UserLoginAnnotation
    public R logoffAccount(HttpServletRequest request){
        request.getAttribute("weiboUser");
        return null;
    }

    /**
     * 忘记密码
     * @param param 找回密码需要的信息(邮箱)
     * @return R
     */
    @PostMapping("/forget")
    public R forgetPassword(Map<String, String> param){
        return accountService.ForgetPassword(param);
    }

    /**
     * 验证 验证码是否正确
     * @param param 信息
     * @return  R
     */
    @GetMapping("/isVerify")
    public R isUserForgetVerify(Map<String, String> param){
        if (param.get("email") == null || param.get("verify") == null){
            return R.error("提交的信息不完全");
        }
        return accountService.isUserForgetVerify(param);
    }

    /**
     * 查询数据库中存在的用户id，存入布隆过滤器
     * @return List
     */
    @PostMapping("/all-id")
    public List<Long> selectAllId(){
        return accountService.selectAllId();
    }

    /**
     * 查询数据库中存在的用户的账号，存入布隆过滤器
     * @return List
     */
    @PostMapping("/all-account")
    public List<String> selectAllAccount(){
        return accountService.selectAllAccount();
    }

    /**
     * 修改密码
     * @param request   当前用户
     * @param param 新旧密码
     * @return  R
     */
    @PostMapping("/update-pwd")
    @UserLoginAnnotation
    public R updateUserPassword(HttpServletRequest request, @RequestBody Map<String, Object> param){
        User user = (User) request.getAttribute("weiboUser");
        return accountService.updateUserPassword(user.getId(), param);
    }


}
