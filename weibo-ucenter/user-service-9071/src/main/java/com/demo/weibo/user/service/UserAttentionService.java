package com.demo.weibo.user.service;

import com.demo.weibo.common.entity.UserAttention;
import com.demo.weibo.common.util.R;

public interface UserAttentionService {

    R addUserAttention(UserAttention userAttention);

    R cancelUserAttention(UserAttention userAttention);

    R findAllUserAttention(Long uId);

}
