package com.demo.weibo.signup.controller;

import com.demo.weibo.common.entity.User;
import com.demo.weibo.common.util.FormatUtil;
import com.demo.weibo.common.util.ObjectUtil;
import com.demo.weibo.common.util.R;
import com.demo.weibo.signup.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 注册
 */
@RestController
@RequestMapping("/sign/register-service")
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    /**
     * 判断账号是否存在
     * @param account 账号
     * @return R
     */
    @GetMapping("/account-exist")
    public Object accountExist(@RequestParam("account") String account){
        if (!FormatUtil.checkAccount(account)){
            return R.error("账号格式不正确");
        }
        return registerService.accountExist(account) ? R.error("该账号存在") : R.ok("该账号可以使用");
    }

    /**
     * 判断邮箱是否已被使用
     * @param email 邮箱
     * @return R
     */
    @GetMapping("/email-exist")
    public Object emailExist(@RequestParam("email") String email){
        if (!FormatUtil.checkEmail(email)){
            return R.error("邮箱格式不正确");
        }
        return registerService.emailExist(email) ? R.error("该邮箱已被使用") : R.ok("该邮箱可以使用");
    }

    /**
     * 用户注册
     * @param params    注册需要的相关信息
     * account password email
     * @return R
     */
    @PostMapping("/register")
    public Object register(@RequestBody Map<String, String> params){
        if (!FormatUtil.checkAccount(params.get("account"))){
            return R.error("账号格式不正确");
        }
        if (!FormatUtil.checkEmail(params.get("email"))){
            return R.error("邮箱格式不正确");
        }

        User user = new User();
        user.setAccount(params.get("account")).setPassword(params.get("password"));
        if (!ObjectUtil.checkFieldsNotEmptyIncludes(user, "account", "password") || params.get("email") == null || params.get("email").equals("")){
            return R.error("您提交的信息不全！");
        }
        return registerService.registerUser(user, params.get("email"), params.get("email_verify"));
    }

}
