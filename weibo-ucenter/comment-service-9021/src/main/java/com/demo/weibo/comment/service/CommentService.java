package com.demo.weibo.comment.service;

import com.demo.weibo.common.entity.mongo.CommentMongo_1;
import com.demo.weibo.common.util.R;

import java.util.List;
import java.util.Map;

public interface CommentService {

    R addComment(Map<String, Object> params);

    R removeComment(Map<String, Object> params);

    R selectAllComment(Long cId, Integer strategy);

}
