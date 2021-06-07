package com.demo.weibo.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.demo.weibo.common.entity.UserAttention;
import com.demo.weibo.common.entity.UserDetail;
import com.demo.weibo.common.entity.mongo.UserAttentionMongo;
import com.demo.weibo.common.entity.msg.U2;
import com.demo.weibo.common.util.R;
import com.demo.weibo.common.entity.msg.AttentionMessage;
import com.demo.weibo.user.mapper.UserDetailMapper;
import com.demo.weibo.user.service.UserAttentionService;
import com.demo.weibo.user.service.impl.mongo.AttentionMongoComponent;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@SuppressWarnings("all")
public class UserAttentionServiceImpl implements UserAttentionService {

    @Autowired
    private AttentionMongoComponent attentionMongoComponent;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private UserDetailMapper userDetailMapper;


    /**
     * 添加关注
     * @param userAttention
     * @return
     */
    @Override
    @Transactional
    public R addUserAttention(UserAttention userAttention) {
        //查询此id为主键的集合是否已经创建
        UserAttention attention = attentionMongoComponent.selectUserAttention(userAttention);
        //获取UserAttention对象中的数组对象
        List<UserAttentionMongo> list = userAttention.getAttentionList();
        UserAttentionMongo userAttentionMongo = list.get(0);
        //查询当前用户是否关注了另一个用户
        JSONObject object = attentionMongoComponent.selectList(userAttention.getU1Id(), userAttentionMongo.getU2Id());
        if (object != null){
            return R.error("已关注该用户，请不要重复关注");
        }
        //查询被关注的用户是否关注了当前用户
        JSONObject jsonObject = attentionMongoComponent.selectList(userAttentionMongo.getU2Id(), userAttention.getU1Id());
//        System.out.println("被关注的：" + jsonObject);
        if (jsonObject == null){ // 被关注用户没有关注当前用户
            //设置关注状态为1，即单向关注
            userAttentionMongo.setACode(1);
            list.set(0, userAttentionMongo);
        }else {
            //设置关注状态为2，即互相关注
            userAttentionMongo.setACode(2);
            list.set(0, userAttentionMongo);
            //更新被关注的用户对当前用户的关注状态为2(互相关注)
            Query query = Query.query(Criteria.where("_id")
                    .is(userAttentionMongo.getU2Id())
            .and("attentionList._id").is(userAttention.getU1Id()));
            Update update = Update.update("attentionList.$.aCode", 2);
            mongoTemplate.updateFirst(query, update, UserAttentionMongo.class);
        }
        //将数组对象存入UserAttention对象中
        userAttention.setAttentionList(list);
        //从redis缓存获取当前用户的所有信息
        UserDetail userDetail = (UserDetail) redisTemplate.opsForValue().get("UserDetail:" + userAttention.getU1Id());
        if (userDetail == null){
            //缓存没有，查数据库
            QueryWrapper<UserDetail> wrapper = new QueryWrapper<>();
            userDetail = userDetailMapper.selectOne(wrapper.eq("u_id", userAttention.getU1Id()));
            if (userDetail == null){
                //查询数据库中也没有
                return R.error("出错了，请进行反馈");
            }
        }
        //将当前用户的关注消息存到mongodb，以便于推送给被关注用户
        AttentionMessage attentionMessage = new AttentionMessage();
        attentionMessage.setU1Id(userAttentionMongo.getU2Id());
        List<U2> u2List = new ArrayList<>();
        U2 u2 = new U2();
        u2.setU2Id(userAttention.getU1Id()).setContent(userDetail.getNickname() + "关注了你").setCode(0);
        u2List.add(u2);
        System.out.println(u2);
        attentionMessage.setMsgList(u2List);
        mongoTemplate.save(attentionMessage);

        //判断此id为主键的集合是否已经创建
        if (attention == null) {
            //新建当前用户id为主键的集合
            mongoTemplate.save(userAttention);
            //获取当前用户关注的人数并更新
            userDetail.setAttention(userDetail.getAttention()+1);
            //重新存进缓存
            redisTemplate.opsForValue().set("UserDetail:" + userDetail.getUId(), userDetail);
            return R.ok("关注成功");
        }
        //该用户的集合已经创建
        //条件为集合的_id为当前用户id
        Query query = Query.query(Criteria.where("_id").is(userAttention.getU1Id()));
        Update update = new Update();
        update.addToSet("attentionList", userAttentionMongo);
        UpdateResult result = mongoTemplate.upsert(query, update,UserAttentionMongo.class);
        return R.ok("关注成功");
    }

    /**
     * 取消关注
     * @param userAttention
     * @return
     */
    @Override
    @Transactional
    public R cancelUserAttention(UserAttention userAttention) {
        //查询该用户的集合是否已经创建
        UserAttention attention = attentionMongoComponent.selectUserAttention(userAttention);
        if (attention == null){  // 没有创建
            return R.error("你没有关注该用户");
        }

        //获取UserAttention对象中的数组对象
        List<UserAttentionMongo> list = userAttention.getAttentionList();
        UserAttentionMongo userAttentionMongo = list.get(0);

        //查询当前用户是否关注了另一个用户
        JSONObject object = attentionMongoComponent.selectList(userAttention.getU1Id(), userAttentionMongo.getU2Id());
        if (object != null){ //已关注另一个用户
            //删除对另一个用户的关注信息
            Query query = Query.query(Criteria.where("_id").is(userAttention.getU1Id()));
            Update update = new Update();
            Document document=new Document("_id", userAttentionMongo.getU2Id());
            //使用pull()删除document中条件的内嵌数组的一个集合
            update.pull("attentionList", document);
            UpdateResult result = mongoTemplate.updateFirst(query, update, UserAttentionMongo.class);

            //从redis缓存获取当前用户的所有信息
            UserDetail userDetail = (UserDetail) redisTemplate.opsForValue().get("UserDetail:" + userAttention.getU1Id());
            //获取当前用户关注的人数并更新
            userDetail.setAttention(userDetail.getAttention()-1);
            //重新存进缓存
            redisTemplate.opsForValue().set("UserDetail:" + userDetail.getUId(), userDetail);

            //查询被关注的用户是否关注了当前用户
            JSONObject jsonObject = attentionMongoComponent.selectList(userAttentionMongo.getU2Id(), userAttention.getU1Id());
            if (jsonObject != null){ // 被关注用户关注了当前用户
                //设置被关注用户对当前用户的关注状态为1，即单向关注
                update = new Update();
                update.set("attentionList.$.aCode", 1);
                query = new Query(Criteria.where("_id").is(userAttentionMongo.getU2Id())
                        .and("attentionList._id").is(userAttention.getU1Id()));
                mongoTemplate.updateFirst(query, update, UserAttentionMongo.class);
                return R.ok("取消关注成功");
            }
        }
        return R.error("你没有关注该用户，无法取消关注");
    }

    @Override
    public R findAllUserAttention(Long uId) {
        List<JSONObject> attentionList = attentionMongoComponent.selectAttentionList(uId);
        if (attentionList != null && attentionList.size() > 0){
            return R.ok("查询成功").addData("attentionList", attentionList);
        }
        return R.error("您没有关注任何用户");
    }
}
