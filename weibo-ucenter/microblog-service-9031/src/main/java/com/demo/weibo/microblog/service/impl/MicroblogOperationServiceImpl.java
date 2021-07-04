package com.demo.weibo.microblog.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.demo.weibo.common.entity.Microblog;
import com.demo.weibo.common.entity.MicroblogOperation;
import com.demo.weibo.common.entity.mongo.MicroblogPojo;
import com.demo.weibo.common.util.R;
import com.demo.weibo.microblog.mapper.MicroblogMapper;
import com.demo.weibo.microblog.service.MicroblogOperationService;
import com.demo.weibo.microblog.util.MicroblogComponent;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 微博操作业务层
 */
@Service
@Transactional
public class MicroblogOperationServiceImpl implements MicroblogOperationService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MicroblogComponent microblogComponent;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private MicroblogMapper microblogMapper;

    @Override
    public R addLikeWeibo(Long uId, Long cId) {
        //从缓存查询微博
        Microblog microblog = (Microblog) redisTemplate.opsForValue().get("weibo:" + cId);
        if (microblog == null){
            //缓存没有，查数据库
            microblog = microblogMapper.selectById(cId);
        }

        //获取微博作者id
        Long u1Id = microblog.getUId();

        //查看mongodb是否创建了该微博的集合
        MicroblogOperation microblogOperation = mongoTemplate.findById(cId, MicroblogOperation.class);

        List<MicroblogPojo> likeList = null;

        MicroblogPojo microblogPojo = null;

        //没有创建,则新建对象存到mongodb
        if (microblogOperation == null){

            microblogOperation = new MicroblogOperation();
            likeList = new ArrayList<>();
            microblogPojo = new MicroblogPojo();
            microblogPojo.setUId(uId).setCode(1);
            likeList.add(microblogPojo);
            microblogOperation.setCId(cId).setMId(u1Id).setLikeList(likeList);
            //存入mongodb
            mongoTemplate.save(microblogOperation);

        }else {

            //获取集合的likeList的一个对象数组
            JSONObject object = microblogComponent.selectList(cId, uId, "likeList");

            if (object != null){

                return R.error("请不要重复点赞");

            }else{
                microblogPojo = new MicroblogPojo();
                microblogPojo.setUId(uId).setCode(1);

                //条件: 集合的_id为微博id
                Query query = Query.query(Criteria.where("_id").is(cId));
                Update update = new Update();
                update.addToSet("likeList", microblogPojo);

                mongoTemplate.upsert(query, update, MicroblogPojo.class);

            }
        }

        //将微博的点赞数+1，然后存回缓存
        microblog.setCLikes(microblog.getCLikes()+1);
        redisTemplate.opsForValue().set("weibo:" + microblog.getId(), microblog);

        //存放点赞消息到用户的消息集合
        boolean res = microblogComponent.addLikeMessage(u1Id, uId);
        if (!res){
            return R.ok("点赞消息同步失败");
        }

        return R.ok("点赞成功");
    }

    @Override
    public R cancelLikeWeibo(Long uId, Long cId) {
        //从缓存查询微博
        Microblog microblog = (Microblog) redisTemplate.opsForValue().get("weibo:" + cId);
        if (microblog == null) {
            microblog = microblogMapper.selectById(cId);
        }

        //查看mongodb是否创建了该微博的集合
        MicroblogOperation microblogMongo = mongoTemplate.findById(cId, MicroblogOperation.class);
        if (microblogMongo == null){
            //mongodb不存在该微博的数据
            return R.error("出错了，微博不存在");
        }

        //获取当前微博id集合的likeList的一个对象
        JSONObject object = microblogComponent.selectList(cId, uId, "likeList");
        //判断是否为空
        if (object == null){
            //为null，即没有点赞过
            return R.error("取消点赞失败，你没有点赞过该微博");
        }

        //不为空，已点赞，则删除，取消点赞
        Update update = new Update();
        Document document=new Document("_id",uId);
        update.pull("likeList", document);
        Query query = new Query(Criteria.where("_id").is(cId));
        mongoTemplate.updateFirst(query, update, MicroblogOperation.class);

        //将微博的点赞数-1，然后存回redis
        microblog.setCLikes(microblog.getCLikes()-1);
        redisTemplate.opsForValue().set("weibo:" + microblog.getId(), microblog);

        return R.ok("取消点赞成功");
    }

    @Override
    public boolean isLikeMicroblog(Long cId, Long uId) {
        JSONObject object = microblogComponent.selectList(cId, uId, "likeList");
        return object != null;
    }


}
