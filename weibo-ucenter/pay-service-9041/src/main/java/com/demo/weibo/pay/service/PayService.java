package com.demo.weibo.pay.service;

import com.demo.weibo.common.entity.UserOrder;
import com.demo.weibo.common.util.R;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

public interface PayService {

    String createVipOrder(UserOrder userOrder);

    R getVipResult(HttpServletRequest request) throws UnsupportedEncodingException;
}
