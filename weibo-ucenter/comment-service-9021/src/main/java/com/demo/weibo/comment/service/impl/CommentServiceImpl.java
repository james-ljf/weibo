package com.demo.weibo.comment.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.demo.weibo.comment.service.CommentService;
import com.demo.weibo.comment.util.CommentComponent;
import com.demo.weibo.common.entity.Microblog;
import com.demo.weibo.common.entity.MicroblogComment;
import com.demo.weibo.common.entity.UserDetail;
import com.demo.weibo.common.entity.mongo.CommentMongo_1;
import com.demo.weibo.common.util.DateUtil;
import com.demo.weibo.common.util.R;
import com.demo.weibo.common.util.id.IdGenerator;
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

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CommentComponent commentComponent;

    @Override
    @Transactional
    public R addComment(Map<String, Object> params) {
        //查询微博是否存在
        Long cId = (Long) params.get("cId");
        Microblog microblog = (Microblog) redisTemplate.opsForValue().get("weibo:" + cId);
        if (microblog == null){
            return R.error("出错了，该微博不存在");
        }

        //去mongodb查询微博评论
        MicroblogComment microblogComment = mongoTemplate.findById(cId, MicroblogComment.class);

        //获取评论者的详细信息
        UserDetail userDetail = (UserDetail) redisTemplate.opsForValue().get("UserDetail:" + params.get("uId"));

        //一级评论实体
        CommentMongo_1 commentMongo_1 = new CommentMongo_1();
        commentMongo_1.setCommentId(IdGenerator.snowflakeId())
                .setUId((Long) params.get("uId"))
                .setNickname(userDetail.getNickname())
                .setAvatar(userDetail.getAvatar())
                .setContent((String) params.get("content"))
                .setDate(DateUtil.getTime())
                .setLikeList(null);

        //判断该微博是否有评论
        if (microblogComment == null){
            //如果该微博没有评论，则新建该微博的评论集合
            microblogComment = new MicroblogComment();
            List<CommentMongo_1> list = new ArrayList<>();
            list.add(commentMongo_1);
            microblogComment.setCId(cId).setCommentList(list);
            mongoTemplate.save(microblogComment);

        }else{

            //如果该微博存在评论，则将新评论添加到评论集合的一级评论数组里
            Query query = Query.query(Criteria.where("_id").is(microblogComment.getCId()));
            Update update = new Update();
            update.addToSet("commentList", commentMongo_1);
            mongoTemplate.upsert(query, update, CommentMongo_1.class);
        }

        //微博的评论数+1，并更新到缓存
        microblog.setCComment(microblog.getCComment()+1);
        redisTemplate.opsForValue().set("weibo:" + microblog.getId(), microblog);

        //将用户评论微博的消息存到mongo消息集合
        boolean res = commentComponent.addCommentMessage(microblog.getUId(), commentMongo_1.getUId());
        if (!res){
            System.out.println("消息同步失败");
        }

        //携带新的评论返回渲染前端
        return R.ok("评论成功").addData("commentOne", commentMongo_1);
    }

    @Override
    public R removeComment(Map<String, Object> params) {
        //微博id
        Long cId = (Long) params.get("cId");

        //评论id
        Long commentId = (Long) params.get("commentId");

        //判断微博是否存在
        Microblog microblog = (Microblog) redisTemplate.opsForValue().get("weibo:" + cId);
        if (microblog == null){
            return R.error("出错了，微博不存在");
        }

        //去mongodb查询该评论是否存在
        JSONObject object = commentComponent.selectList(cId, commentId, "commentList");
        if (object == null){
            return R.error("出错了，该微博没有评论");
        }

        //对这条评论进行删除
        Query query = Query.query(Criteria.where("_id").is(cId));
        Update update = new Update();
        Document document=new Document("_id", commentId);
        //使用pull()删除document中条件的内嵌数组的一个集合
        update.pull("commentList", document);
        mongoTemplate.updateFirst(query, update, CommentMongo_1.class);

        //更新当前微博的评论数量到缓存里
        microblog.setCComment(microblog.getCComment()-1);
        redisTemplate.opsForValue().set("weibo:" + microblog.getId(), microblog);

        return R.ok("删除评论成功");
    }

    @Override
    public List<CommentMongo_1> selectAllComment(Long cId, Integer strategy) {
        List<CommentMongo_1> objectList = commentComponent.selectCommentList(cId);
        if (objectList == null){
            return null;
        }

        //判断排序策略
        if (strategy == 0){
            //按照评论时间排序
            objectList.sort(Comparator.comparing(CommentMongo_1::getDate).reversed());
        }else {
            //按照评论热度(点赞数)排序
            objectList.sort(new Comparator<CommentMongo_1>() {
                @Override
                public int compare(CommentMongo_1 o1, CommentMongo_1 o2) {
                    return o2.getLikeList().size() - o1.getLikeList().size();
                }
            });
        }

        return objectList;
    }

    //测试重写比较器Comparator的compare方法排序
    public static void main(String[] args){
        List<CommentMongo_1> list = new ArrayList<>();

        CommentMongo_1 commentMongo1 = new CommentMongo_1();
        commentMongo1.setUId(7777777L).setNickname("第1个");
        List<Long> likeList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            likeList.add(RandomUtil.randomLong(1,10));
        }
        commentMongo1.setLikeList(likeList);
        list.add(commentMongo1);

        commentMongo1 = new CommentMongo_1();
        commentMongo1.setUId(6666666L).setNickname("第2个");
        likeList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            likeList.add(RandomUtil.randomLong(1,10));
        }
        commentMongo1.setLikeList(likeList);
        list.add(commentMongo1);

        commentMongo1 = new CommentMongo_1();
        commentMongo1.setUId(688886L).setNickname("第3个");
        likeList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            likeList.add(RandomUtil.randomLong(1,10));
        }
        commentMongo1.setLikeList(likeList);
        list.add(commentMongo1);

        //按照热度(点赞数)排序
        list.sort(new Comparator<CommentMongo_1>() {
            @Override
            public int compare(CommentMongo_1 o1, CommentMongo_1 o2) {
                return o2.getLikeList().size() - o1.getLikeList().size();
            }
        });

        System.out.println(list);
    }

}
