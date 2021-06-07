package com.demo.weibo.user.service.impl.mongo;

import com.alibaba.fastjson.JSONObject;
import com.demo.weibo.common.entity.UserAttention;
//import com.demo.weibo.user.repository.UserAttentionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AttentionMongoComponent {

    @Autowired
    private MongoTemplate mongoTemplate;

  //  @Autowired
   // private UserAttentionRepository attentionRepository;

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
}
