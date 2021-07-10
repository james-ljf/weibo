package com.demo.weibo.microblog.util;

import com.alibaba.fastjson.JSONObject;
import com.demo.weibo.common.entity.MicroblogOperation;
import com.demo.weibo.common.entity.UserDetail;
import com.demo.weibo.common.entity.mongo.MicroblogPojo;
import com.demo.weibo.common.entity.msg.UserMessage;
import com.demo.weibo.common.entity.msg.U2;
import com.demo.weibo.common.util.DateUtil;
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
import java.util.Date;
import java.util.List;

@Component
public class MicroblogComponent {

    @Autowired
    private MongoTemplate mongoTemplate;



    /**
     * 查询mongodb集合内嵌数组中的某个对象
     * @param id1   集合id
     * @param id2 内嵌对象id
     * @param list 要查询的内嵌数组名
     * @return JSONObject
     */
    public MicroblogPojo selectList(Long id1, Long id2, String list){
        //存放返回的json对象
        MicroblogPojo a = null;
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
        AggregationResults<MicroblogPojo> reminds = mongoTemplate
                .aggregate(aggregation, "WeiboAll", MicroblogPojo.class);
        List<MicroblogPojo> mappedResults = reminds.getMappedResults();
        if (mappedResults.size() > 0) {
            //获取attentionList数组的一个对象
            a  = mappedResults.get(0);
        }
        return a;
    }

    /**
     * 返回mongodb内嵌数组
     * @param uId
     * @return
     */
    public List<MicroblogPojo> selectLikeList(Long uId){
        //封装对象列表查询条件
        List<AggregationOperation> commonOperations = new ArrayList<>();
        //指定查询主文档
        MatchOperation match = Aggregation.match(Criteria.where("_id").is(uId));
        commonOperations.add(match);
        //指定投影，返回哪些字段
        ProjectionOperation project = Aggregation.project("likeList");
        commonOperations.add(project);
        //创建管道查询对象
        Aggregation aggregation = Aggregation.newAggregation(commonOperations);
        AggregationResults<MicroblogPojo> reminds = mongoTemplate
                .aggregate(aggregation, "WeiboAll", MicroblogPojo.class);
        //返回 List<JSONObject>类型 的查询结果
        return reminds.getMappedResults();
    }

    /**
     * 将当前用户的点赞消息存到mongodb，以便于推送给微博发布者
     * @param u1Id 微博发布者id
     * @param u2Id  点赞的用户的id
     * @return boolean
     */
    public boolean addLikeMessage(Long u1Id, Long u2Id, UserDetail userDetail){

        if (userDetail == null){
            return false;
        }
        //判断mongodb是否存在该用户的消息集合
        UserMessage userMessage = mongoTemplate.findOne(Query.query(Criteria.where("_id").is(u1Id)), UserMessage.class);
        //创建U2对象，数组形式存放消息
        U2 u2 = new U2();
        u2.setU2Id(u2Id).setContent(userDetail.getNickname() + "点赞了你的微博").setCode(0).setDate(DateUtil.dateTime(new Date()));
        if (userMessage == null) {
            //不存在，则创建
            userMessage = new UserMessage();
            userMessage.setU1Id(u1Id);
            userMessage.setMsgList(new ArrayList<>());
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
