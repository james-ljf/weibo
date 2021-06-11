package com.demo.weibo.microblog.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.demo.weibo.common.entity.Microblog;
import com.demo.weibo.common.entity.MicroblogOperation;
import com.demo.weibo.common.entity.mongo.MicroblogPojo;
import com.demo.weibo.common.util.R;
import com.demo.weibo.microblog.mapper.MicroblogRepository;
import com.demo.weibo.microblog.service.MicroblogOperationService;
import com.demo.weibo.microblog.util.MicroblogComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MicroblogOperationServiceImpl implements MicroblogOperationService {

    @Autowired
    private MicroblogRepository microblogRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MicroblogComponent microblogComponent;

    @Override
    public R addLikeWeibo(Long uId, Long cId) {
        //从es查询微博
        Optional<Microblog> optional = microblogRepository.findById(cId);
        Microblog microblog = optional.get();
        //获取微博作者id
        Long u1Id = microblog.getUId();
        //查看mongodb是否创建了该微博的集合
        MicroblogOperation microblogMongo = mongoTemplate.findById(cId, MicroblogOperation.class);
        if (microblogMongo == null){
            //没有创建,则新建对象存到mongodb
            microblogMongo = new MicroblogOperation();
            List<MicroblogPojo> likeList = new ArrayList<>();
            MicroblogPojo microblogPojo = new MicroblogPojo();
            microblogPojo.setUId(uId).setCode(1);
            microblogMongo.setCId(cId).setMId(u1Id).setLikeList(likeList);
            //存入mongodb
            mongoTemplate.save(microblogMongo);
        }
        //获取集合的likeList的一个对象数组
        JSONObject object = microblogComponent.selectList(cId, uId);
        //判断code值是否为1
        if (object.get("code").equals(1)){
            return R.error("请不要重复点赞");
        }
        //code值为0，没有点赞，则修改状态为1，即进行点赞
        Update update = new Update();
        update.set("likeList.$.code", 1);
        Query query = new Query(Criteria.where("_id").is(cId)
                .and("likeList._id").is(uId));
        mongoTemplate.updateFirst(query, update, MicroblogOperation.class);
        //将微博的点赞数+1，然后存回es
        microblog.setCLikes(microblog.getCLikes() + 1);
        microblogRepository.save(microblog);
        boolean res = microblogComponent.addLikeMessage(u1Id, uId);
        return R.ok("点赞成功");
    }

    @Override
    public R cancelLikeWeibo(Long uId, Long cId) {
        //从es查询微博
        Optional<Microblog> optional = microblogRepository.findById(cId);
        Microblog microblog = optional.get();
        //获取微博作者id
        Long u1Id = microblog.getUId();
        //查看mongodb是否创建了该微博的集合
        MicroblogOperation microblogMongo = mongoTemplate.findById(cId, MicroblogOperation.class);
        if (microblogMongo == null){
            //mongodb不存在该微博的数据
            return R.error("出错了，微博不存在");
        }
        //获取当前微博id集合的likeList的一个对象数组
        JSONObject object = microblogComponent.selectList(cId, uId);
        //判断code值是否为1
        if (object.get("code").equals(0)){
            //为0，即没有点赞过
            return R.error("取消点赞失败，你没有点赞过该微博");
        }
        //为1，已点赞，则修改code值为0，取消点赞
        Update update = new Update();
        update.set("likeList.$.code", 0);
        Query query = new Query(Criteria.where("_id").is(cId)
                .and("likeList._id").is(uId));
        mongoTemplate.updateFirst(query, update, MicroblogOperation.class);
        //将微博的点赞数-1，然后存回es
        microblog.setCLikes(microblog.getCLikes() - 1);
        microblogRepository.save(microblog);
        return R.ok("取消点赞成功");
    }


}
