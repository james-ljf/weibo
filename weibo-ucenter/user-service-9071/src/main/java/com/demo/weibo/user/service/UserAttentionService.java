package com.demo.weibo.user.service;

import com.alibaba.fastjson.JSONObject;
import com.demo.weibo.common.entity.UserAttention;
import com.demo.weibo.common.util.R;

import java.util.List;

public interface UserAttentionService {

    R addUserAttention(UserAttention userAttention);

    R cancelUserAttention(UserAttention userAttention);

    R findAllUserAttention(Long uId);

    List<JSONObject> findAllMyAttention(Long uId);

    R findAllUserFriend(Long uId);

    R findAllUserFans(Long uId);

}
