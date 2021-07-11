package com.demo.weibo.vip.controller;

import com.demo.weibo.common.annotation.UserLoginAnnotation;
import com.demo.weibo.common.entity.User;
import com.demo.weibo.common.entity.UserOrder;
import com.demo.weibo.common.util.R;
import com.demo.weibo.vip.service.VipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户会员信息
 */
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

    /**
     * 兑换物品
     * @param request   获取当前用户
     * @param gId   物品id
     * @return  R
     */
    @GetMapping("/exchange/{gId}")
    @UserLoginAnnotation
    public R exchangeVipGoods(HttpServletRequest request, @PathVariable Long gId){
        User user = (User) request.getAttribute("weiboUser");
        return vipService.exchangeVipGoods(user.getId(), gId);
    }

    /**
     * 查询当前用户兑换的物品
     * @param request   获取当前用户
     * @return  R
     */
    @PostMapping("/find-goods")
    @UserLoginAnnotation
    public R selectMyVipGoods(HttpServletRequest request){
        User user = (User) request.getAttribute("weiboUser");
        return vipService.selectMyVipGoods(user.getId());
    }


}

