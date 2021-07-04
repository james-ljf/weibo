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
     * 添加头像
     * @param request 获取用户id
     * @return r
     */

    @GetMapping("/add-avatar")
    @UserLoginAnnotation
    public R uploadUserAvatar(HttpServletRequest request, @RequestParam("avatar") String avatar){
        User user = (User) request.getAttribute("weiboUser");
        if(user == null){
            return R.error("请先登录");
        }
        return userInfoService.uploadUserAvatar(user.getId(), avatar);
    }
}
