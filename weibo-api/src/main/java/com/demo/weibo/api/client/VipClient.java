package com.demo.weibo.api.client;

import com.demo.weibo.common.entity.UserOrder;
import com.demo.weibo.common.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("vip-service")
public interface VipClient {

    @PostMapping("/vip/recharge-success")
    R rechargeVip(@RequestBody UserOrder userOrder);

}
