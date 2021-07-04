package com.demo.weibo.pay.controller;

import com.demo.weibo.common.annotation.UserLoginAnnotation;
import com.demo.weibo.common.entity.User;
import com.demo.weibo.common.entity.UserOrder;
import com.demo.weibo.common.util.R;
import com.demo.weibo.common.util.id.IdGenerator;
import com.demo.weibo.pay.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Date;

@RestController
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private PayService payService;

    /**
     * 创建订单并拉取支付页面
     * @param request   请求
     * @param userOrder 订单实体
     * @return String
     */
    @PostMapping("/create-order")
    @UserLoginAnnotation
    public String getVipPayUrl(HttpServletRequest request, @RequestBody UserOrder userOrder, HttpServletResponse response) {
        User user = (User) request.getAttribute("weiboUser");

        //订单id
        long oId = IdGenerator.snowflakeId(4,4);
        userOrder.setOId(Long.toString(oId)).setUId(user.getId()).setOCreateTime(new Date());

        String url = payService.createVipOrder(userOrder);
        response.setContentType("text/html;charset=utf-8");
        //返回订单页面
        return url;
    }

    /**
     * 支付成功的异步通知回调
     * @param request   回调的请求
     * @return  R
     */
    @PostMapping("/add-order")
    public R getVipResult(HttpServletRequest request) throws UnsupportedEncodingException {
        return payService.getVipResult(request);
    }


}
