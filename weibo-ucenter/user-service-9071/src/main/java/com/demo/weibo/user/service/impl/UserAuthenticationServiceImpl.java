package com.demo.weibo.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.demo.weibo.common.entity.UserAuthentication;
import com.demo.weibo.common.entity.UserDetail;
import com.demo.weibo.common.util.R;
import com.demo.weibo.user.mapper.UserAuthenticationMapper;
import com.demo.weibo.user.mapper.UserDetailMapper;
import com.demo.weibo.user.service.UserAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

    @Autowired
    private UserAuthenticationMapper userAuthenticationMapper;

    @Autowired
    private UserDetailMapper userDetailMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public R addAuthentication(UserAuthentication userAuthentication) {
        //查询是否已经申请了认证
        UserAuthentication userAuthentication_1 = userAuthenticationMapper.selectById(userAuthentication.getUId());
        if (userAuthentication_1 != null) {
            return R.error("您的认证正在审核中，请不要重复申请。");
        }

        //设置参数
        userAuthentication.setAState(2).setATime(new Date());
        int result = userAuthenticationMapper.insert(userAuthentication);

        //从缓存中获取用户信息
        UserDetail userDetail = (UserDetail) redisTemplate.opsForValue().get("UserDetail:" + userAuthentication.getUId());

        if (userDetail == null){
            QueryWrapper<UserDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("u_id", userAuthentication.getUId());
            userDetail = userDetailMapper.selectOne(queryWrapper);
        }
        //设置为认证审批中
        userDetail.setAuthentication(2);

        //存回缓存
        redisTemplate.opsForValue().set("UserDetail" + userDetail.getUId(), userDetail);

        //更新数据库
        int res = userDetailMapper.update(userDetail, new UpdateWrapper<UserDetail>().eq("u_id", userDetail.getUId()));

        return result > 0 && res > 0 ? R.ok("申请认证成功") : R.error("申请认证失败，如有疑问可进行反馈");
    }

    @Override
    public R selectAuthentication(Long uId) {

        //查询用户认证信息
        QueryWrapper<UserAuthentication> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("u_id", uId);
        UserAuthentication userAuthentication = userAuthenticationMapper.selectOne(queryWrapper);

        //判断认证状态
        if (userAuthentication.getAState() == 2){
            return R.error("认证已提交官方进行审核，目前正在核对信息");
        }

        if (userAuthentication.getAState() == 1){
            return R.ok("已认证").addData("userAuthentication", userAuthentication);
        }

        return R.error("未认证");
    }

    @Override
    public R selectAuthenticationById(Long uId) {
        //查询用户认证信息
        QueryWrapper<UserAuthentication> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("u_id", uId);
        UserAuthentication userAuthentication = userAuthenticationMapper.selectOne(queryWrapper);

        if (userAuthentication.getAState() == 1){
            return R.ok("已认证").addData("userAuthentication", userAuthentication);
        }

        return R.error("未认证");
    }

}
