package com.demo.weibo.vip.service;

import com.demo.weibo.common.entity.UserOrder;
import com.demo.weibo.common.util.R;

public interface VipService {

    R rechargeVip(UserOrder userOrder);

    R selectVipByUserId(Long uId);
}
