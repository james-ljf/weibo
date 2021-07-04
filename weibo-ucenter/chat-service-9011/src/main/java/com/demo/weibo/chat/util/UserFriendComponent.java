package com.demo.weibo.chat.util;

import com.alibaba.fastjson.JSONObject;
import com.demo.weibo.common.entity.UserAttention;
import com.demo.weibo.common.entity.UserDetail;
import com.demo.weibo.common.entity.msg.U2;
import com.demo.weibo.common.entity.msg.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserFriendComponent {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 根据用户id查询mongodb内嵌数组
     * @param uId   用户id
     * @return  List
     */
    public List<JSONObject> selectFriendList(Long uId){
        //封装对象列表查询条件
        List<AggregationOperation> commonOperations = new ArrayList<>();
        //指定查询主文档
        MatchOperation match = Aggregation.match(Criteria.where("_id").is(uId));
        commonOperations.add(match);
        //指定投影，返回哪些字段
        ProjectionOperation project = Aggregation.project("friendList");
        commonOperations.add(project);
        //创建管道查询对象
        Aggregation aggregation = Aggregation.newAggregation(commonOperations);
        AggregationResults<JSONObject> reminds = mongoTemplate
                .aggregate(aggregation, "UserFriend", JSONObject.class);
        //返回 List<JSONObject>类型 的查询结果
        return reminds.getMappedResults();
    }
}
