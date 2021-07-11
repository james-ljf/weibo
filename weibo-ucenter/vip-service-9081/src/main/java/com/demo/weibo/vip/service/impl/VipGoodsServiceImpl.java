package com.demo.weibo.vip.service.impl;

import com.demo.weibo.common.entity.UserVipIntegral;
import com.demo.weibo.common.util.R;
import com.demo.weibo.vip.mapper.UserVipIntegralMapper;
import com.demo.weibo.vip.service.VipGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class VipGoodsServiceImpl implements VipGoodsService {

    @Autowired
    private UserVipIntegralMapper userVipIntegralMapper;

    @Override
    public R findAllGoods() {

        //数据库查询会员中心物品列表
        List<UserVipIntegral> userVipIntegralList = userVipIntegralMapper.selectList(null);

        if (userVipIntegralList.size() == 0){
            return R.error("没有任何兑换物品");
        }

        return R.ok("查询兑换物品列表成功").addData("vipGoodsList", userVipIntegralList);
    }

}
