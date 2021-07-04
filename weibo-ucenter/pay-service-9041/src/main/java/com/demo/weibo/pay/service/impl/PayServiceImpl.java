package com.demo.weibo.pay.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.demo.weibo.api.client.EmailClient;
import com.demo.weibo.api.client.VipClient;
import com.demo.weibo.common.entity.UserDetail;
import com.demo.weibo.common.entity.UserOrder;
import com.demo.weibo.common.util.R;
import com.demo.weibo.pay.configuration.AlipayConfig;
import com.demo.weibo.pay.mapper.UserOrderMapper;
import com.demo.weibo.pay.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class PayServiceImpl implements PayService {

    //全局对象，存放创建订单时的订单信息
    private static UserOrder USER_ORDER;

    @Autowired
    private EmailClient emailClient;

    @Autowired
    private VipClient vipClient;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private UserOrderMapper userOrderMapper;

    @Override
    public String createVipOrder(UserOrder userOrder) {
        //创建订单
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl,
                AlipayConfig.app_id,
                AlipayConfig.merchant_private_key,
                "json",
                AlipayConfig.charset,
                AlipayConfig.alipay_public_key,
                AlipayConfig.sign_type);
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();

        //设置同步通知和异步通知url
        request.setReturnUrl(AlipayConfig.return_url); //同步
        request.setNotifyUrl(AlipayConfig.notify_url); //异步

        //设置网页支付参数
        String oId = userOrder.getOId();
        String oPrice = userOrder.getOPrice();
        String oName = userOrder.getOName();
        String oDetail = userOrder.getODetail();
        String oTimeOut = "3m";
        request.setBizContent("{\"out_trade_no\":\""+ oId +"\","
                + "\"total_amount\":\""+ oPrice +"\","
                + "\"subject\":\""+ oName +"\","
                + "\"body\":\""+ oDetail +"\","
                + "\"timeout_express\":\""+ oTimeOut +"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        try {
            //使用sdkExecute
            AlipayTradePagePayResponse response = alipayClient.pageExecute(request);
            if(response.isSuccess()){
                log.info("【 Ali pay调用成功 】");
            } else {
                log.info("【 Ali pay调用失败 】");
            }
            USER_ORDER = userOrder;
            //返回页面
            return response.getBody();
        } catch (AlipayApiException e) {
            log.error("【 Ali pay 异常 】", e);
        }
        return "fail";
    }

    @Override
    public R getVipResult(HttpServletRequest request) {
        //订单号
        String outTradeNo = new String(request.getParameter("out_trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        //订单实际金额
        String totalAmount = new String(request.getParameter("total_amount").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        //商户app_id
        String appId = new String(request.getParameter("app_id").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        //交易状态
        String trade_status = new String(request.getParameter("trade_status").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

        //获取创建订单用户的邮箱
        UserDetail userDetail = (UserDetail) redisTemplate.opsForValue().get("UserDetail:" + USER_ORDER.getUId());
        assert userDetail != null;
        //判断异步请求的id、金额、app_id是否和创建订单时的一样，不一样则发送充值失败的邮件
        if (!outTradeNo.equals(USER_ORDER.getOId()) || !AlipayConfig.app_id.equals(appId)){
            //将邮件的内容存到map集合里
            Map<String, String> params = new HashMap<>();
            params.put("to", userDetail.getEmail());
            params.put("content", "很抱歉，你的订单支付出错了，充值会员失败，请重新充值，如若仍出现上述情况，请及时反馈。");
            params.put("strategy", "3");
            //调用发送邮件服务
            emailClient.sendEmail(params);
            return R.error("订单支付异常");
        }

        //判断该笔订单是否已经支付成功,支付成功则执行业务
        if (trade_status.equals("TRADE_SUCCESS")){
            //将邮件的内容存到map集合里
            Map<String, String> params = new HashMap<>();
            params.put("to", userDetail.getEmail());
            params.put("content", "尊敬的用户，您已成功充值会员，如会员功能使用过程中遇到问题，请及时反馈。");
            params.put("strategy", "3");

            //调用添加会员业务
            vipClient.rechargeVip(USER_ORDER);

            //将订单存到数据库
            userOrderMapper.insert(USER_ORDER);

            //调用发送邮件服务
            emailClient.sendEmail(params);
        }

        return R.ok("充值成功");
    }

}
