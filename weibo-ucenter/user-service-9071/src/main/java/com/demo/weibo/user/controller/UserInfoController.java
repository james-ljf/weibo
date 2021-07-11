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
import java.util.List;

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
     * @param userDetail    用户信息
     * @return  Object
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
     * @param email 邮箱
     * @return  UserDetail
     */
    @GetMapping("/select-email")
    public UserDetail selectUserEmail(@RequestParam("email") String email){
        return userInfoService.selectUserEmail(email);
    }

    /**
     * 查询某用户所有信息(已登录)
     * @param uId   用户id
     * @return  R
     */
    @GetMapping("/all/{uId}")
    @UserLoginAnnotation()
    public R selectUserAll(HttpServletRequest request, @PathVariable Long uId){
        User user = (User) request.getAttribute("weiboUser");
        return userInfoService.selectUserAllById(user.getId(), uId);
    }

    /**
     * 查询某用户所有信息(未登录)
     * @param uId   用户id
     * @return  R
     */
    @GetMapping("/user-all/{uId}")
    public R selectUserAll(@PathVariable Long uId){
        return userInfoService.selectUserAllById(0L, uId);
    }

    /**
     * 查询自己的所有信息
     * @param request   获取自己登录信息
     * @return  R
     */
    @GetMapping("/my-all")
    @UserLoginAnnotation
    public R selectUserAll(HttpServletRequest request){
        User user = (User) request.getAttribute("weiboUser");
        return userInfoService.selectUserAll(user.getId());
    }

    /**
     * 查询所有用户信息初始化到缓存
     * @return  List
     */
    @PostMapping("/all")
    public List<UserDetail> selectAll(){
        return userInfoService.selectAll();
    }

    /**
     * 更新用户信息
     * @param request 获取用户id
     * @param userDetail 要修改的信息
     * @return R
     */
    @UserLoginAnnotation
    @PostMapping("/update")
    public R updateUserInfo(HttpServletRequest request, @RequestBody UserDetail userDetail){
        User user = (User) request.getAttribute("weiboUser");
        userDetail.setUId(user.getId());
        return userInfoService.updateUserInfo(userDetail);
    }

    /**
     * 更新用户信息（服务调用）
     * @param userDetail    用户实体
     * @return  R
     */
    @PostMapping("/update-info")
    public R updateUserInfoByPojo(@RequestBody UserDetail userDetail){
        return userInfoService.updateUserInfoByPojo(userDetail);
    }

    /**
     * 添加头像
     * @param request 获取用户id
     * @return r
     */
    @GetMapping("/add-avatar")
    @UserLoginAnnotation
    public R uploadUserAvatar(HttpServletRequest request, @RequestParam("avatar") String avatar){
        User user = (User) request.getAttribute("weiboUser");

        return userInfoService.uploadUserAvatar(user.getId(), avatar);
    }

    /**
     * 选择用户资料封面
     * @param request   获取当前用户
     * @param image 图片
     * @return  R
     */
    @GetMapping("/add-cover")
    @UserLoginAnnotation
    public R addUserCover(HttpServletRequest request, @RequestParam("image") String image){
        User user = (User) request.getAttribute("weiboUser");

        return userInfoService.addUserCover(user.getId(), image);
    }
}
