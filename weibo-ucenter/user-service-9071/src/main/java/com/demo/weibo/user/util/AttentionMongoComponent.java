package com.demo.weibo.user.util;

import com.alibaba.fastjson.JSONObject;
import com.demo.weibo.common.entity.UserAttention;
//import com.demo.weibo.user.repository.UserAttentionRepository;
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
public class AttentionMongoComponent {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * 查询mongodb集合内的数组中的某个对象
     * @param id1
     * @param id2
     * @return
     */
    public JSONObject selectList(Long id1, Long id2){
        //存放返回的json对象
        JSONObject a = null;
        //封装对象列表查询条件
        List<AggregationOperation> commonOperations = new ArrayList<>();
        //1. 指定查询主文档
        MatchOperation match = Aggregation.match(Criteria.where("_id").is(id1));
        commonOperations.add(match);
        //2. 指定投影，返回哪些字段
        ProjectionOperation project = Aggregation.project("attentionList");
        commonOperations.add(project);
        //3. 拆分内嵌文档
        UnwindOperation unwind = Aggregation.unwind("attentionList");
        commonOperations.add(unwind);
        //4. 指定查询子文档
        MatchOperation match2 = Aggregation.match(
                Criteria.where("attentionList._id").is(id2));
        commonOperations.add(match2);

        //创建管道查询对象
        Aggregation aggregation = Aggregation.newAggregation(commonOperations);
        AggregationResults<JSONObject> reminds = mongoTemplate
                .aggregate(aggregation, "UserAttention", JSONObject.class);
        List<JSONObject> mappedResults = reminds.getMappedResults();
        if (mappedResults != null && mappedResults.size() > 0) {
            //获取attentionList数组的一个对象
            a  = JSONObject.parseObject(mappedResults.get(0).getJSONObject("attentionList").toJSONString());
        }
        return a;
    }

    /**
     * 查询该id是否创建了集合
     * @param userAttention
     * @return
     */
    public UserAttention selectUserAttention(UserAttention userAttention){
        //return attentionRepository.findOneById(userAttention.getU1Id());
        Query query = new Query(Criteria.where("_id").is(userAttention.getU1Id()));
        return mongoTemplate.findOne(query, UserAttention.class);
    }

    public List<JSONObject> selectAttentionList(Long uId){
        //封装对象列表查询条件
        List<AggregationOperation> commonOperations = new ArrayList<>();
        //指定查询主文档
        MatchOperation match = Aggregation.match(Criteria.where("_id").is(uId));
        commonOperations.add(match);
        //指定投影，返回哪些字段
        ProjectionOperation project = Aggregation.project("attentionList");
        commonOperations.add(project);
        //创建管道查询对象
        Aggregation aggregation = Aggregation.newAggregation(commonOperations);
        AggregationResults<JSONObject> reminds = mongoTemplate
                .aggregate(aggregation, "UserAttention", JSONObject.class);
        //返回 List<JSONObject>类型 的查询结果
        return reminds.getMappedResults();
    }

    /**
     * 将当前用户的关注消息存到mongodb，以便于推送给被关注的用户
     * @param u1Id 关注的用户的id
     * @param u2Id  被关注的用户的id
     * @return boolean
     */
    @Transactional
    public boolean addAttentionMessage(Long u1Id, Long u2Id){
        //从redis缓存获取当前用户的所有信息
        UserDetail userDetail = (UserDetail) redisTemplate.opsForValue().get("UserDetail:" + u1Id);
        if (userDetail == null){
            return false;
        }
        //判断mongodb是否存在该用户的消息集合
        UserMessage userMessage = mongoTemplate.findById(u2Id, UserMessage.class);
        //创建U2对象，数组形式存放消息
        U2 u2 = new U2();
        u2.setU2Id(u1Id).setContent(userDetail.getNickname() + "关注了你").setCode(0);
        if (userMessage == null) {
            //不存在，则创建
            userMessage = new UserMessage();
            userMessage.setU1Id(u2Id);
            List<U2> u1List = new ArrayList<>();
            u1List.add(u2);
            userMessage.setMsgList(u1List);
            mongoTemplate.save(userMessage);
            return true;
        }
        //存在，则插入到内嵌对象数组里
        Query query = Query.query(Criteria.where("_id").is(u2Id));
        Update update = new Update();
        update.addToSet("msgList", u2);
        mongoTemplate.upsert(query, update, U2.class);
        return true;
    }
}
