package com.demo.weibo.user.controller;

import com.demo.weibo.common.annotation.UserLoginAnnotation;
import com.demo.weibo.common.entity.User;
import com.demo.weibo.common.entity.UserDetail;
import com.demo.weibo.common.util.R;
import com.demo.weibo.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
     * 查询某用户所有信息
     * @param uId
     * @return
     */
    @GetMapping("/all/{uId}")
    public R selectUserAll( @PathVariable Long uId){
        return userInfoService.selectUserAll(uId);
    }

    /**
     * 查询自己的所有信息
     * @param request
     * @return
     */
    @GetMapping("/my-all")
    @UserLoginAnnotation
    public R selectUserAll(HttpServletRequest request){
        User user = (User) request.getAttribute("weiboUser");
        return userInfoService.selectUserAll(user.getId());
    }

    /**
     * 根据用户id更新用户信息
     * @param request 获取用户id
     * @param userDetail 要修改的信息
     * @return r
     */
    @UserLoginAnnotation
    @PostMapping("/update")
    public R updateUserInfo(HttpServletRequest request, @RequestBody UserDetail userDetail){
        User user = (User) request.getAttribute("weiboUser");
        userDetail.setUId(user.getId());
        return userInfoService.updateUserInfo(userDetail);
    }

    /**
     * 上传头像
     * @param request 获取用户id
     * @param file  头像文件
     * @return r
     */
    //@UserLoginAnnotation
    @GetMapping("/add-avatar")
    public R uploadUserAvatar(HttpServletRequest request, MultipartFile file){
        User user = (User) request.getAttribute("weiboUser");
        return userInfoService.uploadUserAvatar(user.getId(), file);
    }
}
