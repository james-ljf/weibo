package com.demo.weibo.vip.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.demo.weibo.common.entity.*;
import com.demo.weibo.common.util.DateUtil;
import com.demo.weibo.common.util.R;
import com.demo.weibo.common.util.id.IdGenerator;
import com.demo.weibo.vip.mapper.UserVipExchangeMapper;
import com.demo.weibo.vip.mapper.UserVipIntegralMapper;
import com.demo.weibo.vip.mapper.UserVipMapper;
import com.demo.weibo.vip.service.VipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class VipServiceImpl implements VipService {

    @Autowired
    private UserVipMapper userVipMapper;

    @Autowired
    private UserVipIntegralMapper userVipIntegralMapper;

    @Autowired
    private UserVipExchangeMapper userVipExchangeMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R rechargeVip(UserOrder userOrder) {
        //查询该用户是否充值过会员
        UserVip userVip = (UserVip) redisTemplate.opsForValue().get("UserVip:" + userOrder.getUId());
        if (userVip == null){
            userVip = userVipMapper.selectById(userOrder.getUId());
        }

        //获取当前时间
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        //将当前时间月份加上充值的套餐的时间
        cal.add(Calendar.MONTH,userOrder.getOPackage());
        //转换成string类型的 yyyyMMdd 格式
        String expireTime = DateUtil.dateTime(cal.getTime());

        //用户没有充值过会员
        if (userVip == null){

            userVip = new UserVip();
            userVip.setUId(userOrder.getUId())
                    .setRechargeTime(date)
                    .setExpireTime(expireTime)
                    .setVipCode(1);
            //添加到数据库
            userVipMapper.insert(userVip);

            //更新用户会员状态为 已充值 1
            UserDetail userDetail = (UserDetail) redisTemplate.opsForValue().get("UserDetail:" + userVip.getUId());
            assert userDetail != null;
            userDetail.setVip(1);
            redisTemplate.opsForValue().set("UserDetail:" + userDetail.getUId(), userDetail);

            //将会员信息放到缓存
            redisTemplate.opsForValue().set("UserVip:" + userVip.getUId(), userVip);


            return R.ok("会员充值成功");
        }

        //用户会员已过期，修改充值、过期时间以及状态,更新到缓存
        if (userVip.getVipCode() == 0){

            userVip.setRechargeTime(date).setExpireTime(expireTime).setVipCode(1);
            redisTemplate.opsForValue().set("UserVip:" + userVip.getUId(), userVip);

            return R.ok("会员充值成功");
        }

        //用户会员正在使用中，即续费，修改过期时间
        //获取到期时间加上套餐时间
        Date vTime = DateUtil.parseDate(userVip.getExpireTime());
        cal.setTime(vTime);
        cal.add(Calendar.MONTH,userOrder.getOPackage());

        //转换成string类型的 yyyyMMdd 格式
        userVip.setExpireTime(DateUtil.dateTime(cal.getTime()));

        //更新缓存
        redisTemplate.opsForValue().set("UserVip:" + userVip.getUId(), userVip);

        return R.ok("续费成功");
    }

    @Override
    public R selectVipByUserId(Long uId) {
        //查询该用户是否充值过会员
        UserVip userVip = (UserVip) redisTemplate.opsForValue().get("UserVip:" + uId);
        if (userVip == null){

            userVip = userVipMapper.selectById(uId);
            if (userVip != null){
                //更新缓存
                redisTemplate.opsForValue().set("UserVip:" + userVip.getUId(), userVip);
            }
        }


        if (userVip == null) {
            return R.error("你没有充值会员");
        }

        //将字符型日期转换为Date类型
        Date date = DateUtil.parseDate(userVip.getExpireTime());
        if ( DateUtil.getDatePoor(date, new Date()) == null ){
            return R.error("您的会员已过期");
        }
        return R.ok("查询成功").addData("userVip", userVip);
    }

    @Override
    @Transactional
    public R exchangeVipGoods(Long uId, Long gId) {

        //查询数据库获取物品信息
        UserVipIntegral userVipIntegral = userVipIntegralMapper.selectById(gId);
        if (userVipIntegral == null){
            return R.error("该物品不存在");
        }

        UserVipExchange userVipExchange = new UserVipExchange();

        //设置兑换会员物品对象的参数
        userVipExchange.setId(IdGenerator.snowflakeId())
                .setUId(uId)
                .setCreateTime(DateUtil.dateTimeMM(new Date()))
                .setVId(userVipIntegral.getId())
                .setVImage(userVipIntegral.getIImage());

        //插入兑换物品数据库
        int result = userVipExchangeMapper.insert(userVipExchange);

        return result > 0 ? R.ok("兑换成功") : R.error("兑换失败");
    }

    @Override
    public R selectMyVipGoods(Long uId) {

        //数据库查自己兑换的物品列表
        QueryWrapper<UserVipExchange> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("u_id", uId);
        List<UserVipExchange> userVipExchangeList = userVipExchangeMapper.selectList(queryWrapper);

        if (userVipExchangeList.size()  == 0){
            return R.error("什么都没兑换");
        }

        return R.ok("背景图查询成功").addData("userGoods", userVipExchangeList);
    }

}
