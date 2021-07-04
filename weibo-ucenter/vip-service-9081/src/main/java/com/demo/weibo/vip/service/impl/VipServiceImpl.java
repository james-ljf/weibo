package com.demo.weibo.vip.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.demo.weibo.common.entity.UserDetail;
import com.demo.weibo.common.entity.UserOrder;
import com.demo.weibo.common.entity.UserVip;
import com.demo.weibo.common.util.DateUtil;
import com.demo.weibo.common.util.R;
import com.demo.weibo.vip.mapper.UserVipMapper;
import com.demo.weibo.vip.service.VipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

@Service
public class VipServiceImpl implements VipService {

    @Autowired
    private UserVipMapper userVipMapper;

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

}
