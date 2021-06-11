package com.demo.weibo.signup.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.weibo.common.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<User> {
}
