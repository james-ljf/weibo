package com.demo.weibo.user.controller;

import com.demo.weibo.common.annotation.UserLoginAnnotation;
import com.demo.weibo.common.entity.UserDetail;
import com.demo.weibo.common.util.R;
import com.demo.weibo.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户信息操作
 */
@RestController
@RequestMapping("/user/info")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 添加email
     * @param userDetail
     * @return
     */
    @PostMapping("/add-info")
    public Object addUserInfo(@RequestBody UserDetail userDetail){
        int result = userInfoService.addUserInfo(userDetail);
        if (result > 0){
            return R.ok("插入email成功");
        }else {
            return R.error("插入失败");
        }
    }

    /**
     * 查询email是否存在
     * @param email
     * @return
     */
    @GetMapping("/select-email")
    public UserDetail selectUserEmail(@RequestParam("email") String email){
        return userInfoService.selectUserEmail(email);
    }

    /**
     * 查询用户所有信息
     * @param uId
     * @return
     */
    @GetMapping("/all")
    public R selectUserAll(HttpServletRequest request, @RequestParam("uId") Long uId){
        return userInfoService.selectUserAll(uId);
    }

    /**
     * 根据用户id更新用户信息
     * @param request
     * @param userDetail
     * @return
     */
    @UserLoginAnnotation
    @PostMapping("/update")
    public R updateUserInfo(HttpServletRequest request, @RequestBody UserDetail userDetail){
        return userInfoService.updateUserInfo(userDetail);
    }
}
