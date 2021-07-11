package com.demo.weibo.vip.service;

import com.demo.weibo.common.entity.UserOrder;
import com.demo.weibo.common.util.R;
import org.springframework.web.bind.annotation.PathVariable;

public interface VipService {

    R rechargeVip(UserOrder userOrder);

    R selectVipByUserId(Long uId);

    R exchangeVipGoods(Long uId, Long gId);

    R selectMyVipGoods(Long uId);
}
