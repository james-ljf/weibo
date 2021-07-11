package com.demo.weibo.comment.util;

import com.alibaba.fastjson.JSONObject;
import com.demo.weibo.common.entity.UserDetail;
import com.demo.weibo.common.entity.mongo.CommentMongo_1;
import com.demo.weibo.common.entity.mongo.MicroblogPojo;
import com.demo.weibo.common.entity.msg.UserMessage;
import com.demo.weibo.common.entity.msg.U2;
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
public class CommentComponent {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * 查询mongodb集合内嵌数组中的某个对象
     * @param id1   集合id
     * @param id2 内嵌对象id
     * @param list 要查询的内嵌数组名
     * @return JSONObject
     */
    public JSONObject selectList(Long id1, Long id2, String list){
        //存放返回的json对象
        JSONObject a = null;
        //封装对象列表查询条件
        List<AggregationOperation> commonOperations = new ArrayList<>();
        //1. 指定查询主文档
        MatchOperation match = Aggregation.match(Criteria.where("_id").is(id1));
        commonOperations.add(match);
        //2. 指定投影，返回哪些字段
        ProjectionOperation project = Aggregation.project(list);
        commonOperations.add(project);
        //3. 拆分内嵌文档
        UnwindOperation unwind = Aggregation.unwind(list);
        commonOperations.add(unwind);
        //4. 指定查询子文档
        MatchOperation match2 = Aggregation.match(
                Criteria.where(list + "._id").is(id2));
        commonOperations.add(match2);

        //创建管道查询对象
        Aggregation aggregation = Aggregation.newAggregation(commonOperations);
        AggregationResults<JSONObject> reminds = mongoTemplate
                .aggregate(aggregation, "Comment", JSONObject.class);
        List<JSONObject> mappedResults = reminds.getMappedResults();
        if (mappedResults.size() > 0) {
            //获取attentionList数组的一个对象
            a  = JSONObject.parseObject(mappedResults.get(0).getJSONObject(list).toJSONString());
        }
        return a;
    }


    /**
     * 返回mongodb内嵌数组
     * @param cId   微博id
     * @return  List
     */
    public List<CommentMongo_1> selectCommentList(Long cId){
        //封装对象列表查询条件
        List<AggregationOperation> commonOperations = new ArrayList<>();
        //指定查询主文档
        MatchOperation match = Aggregation.match(Criteria.where("_id").is(cId));
        commonOperations.add(match);
        //指定投影，返回哪些字段
        ProjectionOperation project = Aggregation.project("commentList");
        commonOperations.add(project);
        //创建管道查询对象
        Aggregation aggregation = Aggregation.newAggregation(commonOperations);
        AggregationResults<JSONObject> reminds = mongoTemplate
                .aggregate(aggregation, "Comment", JSONObject.class);
        //返回 List<JSONObject>类型 的查询结果
        JSONObject jsonObjects = reminds.getMappedResults().get(0);

        //转成对象数组
        List<CommentMongo_1> list = (List<CommentMongo_1>) jsonObjects.get("commentList");

        return list;
    }


    /**
     * 将当前用户的评论消息存到mongodb，以便于推送给微博发布者
     * @param u1Id 微博发布者id
     * @param u2Id  评论的用户的id
     * @return boolean
     */
    public boolean addCommentMessage(Long u1Id, Long u2Id){
        //从redis缓存获取当前用户的所有信息
        UserDetail userDetail = (UserDetail) redisTemplate.opsForValue().get("UserDetail:" + u2Id);
        if (userDetail == null){
            return false;
        }
        //判断mongodb是否存在该用户的消息集合
        UserMessage userMessage = mongoTemplate.findById(u1Id, UserMessage.class);
        //创建U2对象，数组形式存放消息
        U2 u2 = new U2();
        u2.setU2Id(u2Id).setContent(userDetail.getNickname() + "评论了你的微博").setCode(0);
        if (userMessage == null) {
            //不存在，则创建
            userMessage = new UserMessage();
            userMessage.setU1Id(u1Id);
            List<U2> u2List = new ArrayList<>();
            u2List.add(u2);
            userMessage.setMsgList(u2List);
            mongoTemplate.save(userMessage);
            return true;
        }
        //存在，则插入到内嵌对象数组里
        Query query = Query.query(Criteria.where("_id").is(u1Id));
        Update update = new Update();
        update.addToSet("msgList", u2);
        mongoTemplate.upsert(query, update, U2.class);
        return true;
    }
}
