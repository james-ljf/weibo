package com.demo.weibo.vip.controller;

import com.demo.weibo.common.annotation.UserLoginAnnotation;
import com.demo.weibo.common.entity.User;
import com.demo.weibo.common.entity.UserOrder;
import com.demo.weibo.common.util.R;
import com.demo.weibo.vip.service.VipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/vip")
public class VipController {

    @Autowired
    private VipService vipService;

    /**
     * 充值成功，添加会员信息
     * @param userOrder 订单实体
     * @return R
     */
    @PostMapping("/recharge-success")
    public R rechargeVip(@RequestBody UserOrder userOrder){
        return vipService.rechargeVip(userOrder);
    }

    /**
     * 查询用户会员信息
     * @param request  获取用户
     * @return  R
     */
    @GetMapping("/my")
    @UserLoginAnnotation
    public R selectVipByUserId(HttpServletRequest request){
        User user = (User) request.getAttribute("weiboUser");
        return vipService.selectVipByUserId(user.getId());
    }

}

