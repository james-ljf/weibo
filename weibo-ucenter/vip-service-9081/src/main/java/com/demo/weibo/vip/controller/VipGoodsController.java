package com.demo.weibo.vip.controller;

import com.demo.weibo.common.util.R;
import com.demo.weibo.vip.service.VipGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 会员物品
 */
@RestController
@RequestMapping("/vip/goods")
public class VipGoodsController {

    @Autowired
    private VipGoodsService vipGoodsService;

    /**
     * 查询所有会员物品
     * @return  R
     */
    @PostMapping("/find-all")
    public R findAllGoods(){
        return vipGoodsService.findAllGoods();
    }



}
