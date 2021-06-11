package com.demo.weibo.signup.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.demo.weibo.api.client.UserClient;
import com.demo.weibo.common.entity.User;
import com.demo.weibo.common.entity.UserDetail;
import com.demo.weibo.common.entity.UserState;
import com.demo.weibo.common.filter.RedissonBloomFilter;
import com.demo.weibo.common.util.R;
import com.demo.weibo.common.util.id.IdGenerator;
import com.demo.weibo.common.util.ip.IpUtils;
import com.demo.weibo.signup.mapper.UserMapper;
import com.demo.weibo.signup.mapper.UserStateMapper;
import com.demo.weibo.signup.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserClient userClient;

    @Autowired
    private UserStateMapper userStateMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * 账号是否已被注册
     * @param account
     * @return
     */
    @Override
    public boolean accountExist(String account) {
        //先从布隆过滤器判断是否存在该账号
       if(!RedissonBloomFilter.check(account)){
           return true;
       }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        User user = userMapper.selectOne(queryWrapper.eq("account", account));
        return user != null;
    }

    /**
     * 邮箱是否已被注册
     * @param email
     * @return
     */
    @Override
    public boolean emailExist(String email) {
        UserDetail userDetail = userClient.selectUserEmail(email);
        return userDetail != null;
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R registerUser(User user, String email, String email_code) {
        //获取存在redis的验证码
        String redis_verify = stringRedisTemplate.opsForValue().get("userRegisterVerify:" + email);
        if (redis_verify == null || redis_verify.equals("")){
            return R.error("验证码已过期，请重新发送");
        }
        if (!redis_verify.equals(email_code)){
            return R.error("验证码不正确，请重新输入");
        }
        if (accountExist(user.getAccount())){
            return R.error("该账号已被注册");
        }
        if (emailExist(email)){
            return R.error("该邮箱已被注册");
        }
        Long uId = IdGenerator.snowflakeId();
        //判断该id是否已存在布隆过滤器里（极小概率）
        while(RedissonBloomFilter.check(uId.toString())) {
            uId = IdGenerator.snowflakeId();
        }
        Date date = new Date();
        user.setCreate_time(date).setState(1).setId(uId);
        user.setPassword(passwordEncoder.encode(user.getPassword())); //密码加密
        int result = userMapper.insert(user);
        //将注册的用户的id和账号存到布隆过滤器
        RedissonBloomFilter.add(uId.toString());
        RedissonBloomFilter.add(user.getAccount());
        //存用户详细信息表
        UserDetail userDetail = new UserDetail();
        userDetail.setEmail(email).setUId(user.getId());
        //调用user-service服务将用户信息添加
        Object r = userClient.addUserInfo(userDetail);
        //存缓存
        redisTemplate.opsForValue().set("UserDetail:" + userDetail.getUId(), userDetail);
        //存用户登录状态表
        UserState userState = new UserState();
        userState.setUId(user.getId());
        int result1 = userStateMapper.insert(userState);
        return result > 0 && r != null && result1 > 0 ? R.ok("注册成功") : R.error("注册失败");
    }
}
