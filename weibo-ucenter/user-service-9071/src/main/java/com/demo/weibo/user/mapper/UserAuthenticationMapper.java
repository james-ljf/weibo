package com.demo.weibo.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.weibo.common.entity.UserAuthentication;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuthenticationMapper extends BaseMapper<UserAuthentication> {
}
