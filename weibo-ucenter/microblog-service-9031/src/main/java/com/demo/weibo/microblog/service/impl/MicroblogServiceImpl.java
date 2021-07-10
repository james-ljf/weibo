package com.demo.weibo.microblog.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.weibo.api.client.CommentClient;
import com.demo.weibo.api.client.UserClient;
import com.demo.weibo.common.entity.Microblog;
import com.demo.weibo.common.entity.UserDetail;
import com.demo.weibo.common.entity.mongo.CommentMongo_1;
import com.demo.weibo.common.entity.mongo.MicroblogPojo;
import com.demo.weibo.common.entity.mongo.UserAttentionMongo;
import com.demo.weibo.common.entity.msg.Weibo;
import com.demo.weibo.common.util.DateUtil;
import com.demo.weibo.common.util.R;
import com.demo.weibo.common.util.id.IdGenerator;
import com.demo.weibo.microblog.mapper.MicroblogMapper;
import com.demo.weibo.microblog.service.MicroblogService;
import com.demo.weibo.microblog.util.MicroblogComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class MicroblogServiceImpl implements MicroblogService {

    @Autowired
    private MicroblogMapper microblogMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private CommentClient commentClient;

    @Autowired
    private UserClient userClient;

    @Autowired
    private MicroblogComponent microblogComponent;


    @Override
    //@Transactional(rollbackFor = Exception.class)
    public R releaseWeiBo(Long uId, Microblog microblog) {
        //雪花算法生成微博id
        microblog.setId(IdGenerator.snowflakeId()).setUId(uId).setCTime(new Date());

        //添加到数据库
        int result = microblogMapper.insert(microblog);

        //将微博存入缓存
        redisTemplate.opsForValue().set("weibo:" + microblog.getId(), microblog);

        //微博id存入缓存
        redisTemplate.opsForList().leftPush("WeiboID:", microblog.getId());

        Weibo weibo = new Weibo();

        //更新缓存信息，用户发布的微博数加一
        UserDetail userDetail = (UserDetail) redisTemplate.opsForValue().get("UserDetail:" + uId);
        if (userDetail != null){
            weibo.setAvatar(userDetail.getAvatar()).setNickname(userDetail.getNickname());
            int a = userDetail.getArticle();
            userDetail.setArticle(a + 1);
            redisTemplate.opsForValue().set("UserDetail:" + uId, userDetail);

        }

        weibo.setId(microblog.getId())
                .setMicroblog(microblog)
                .setTime(DateUtil.dateTimeMM(microblog.getCTime()))
                .setCode(0)
                .setAttention(0)
                .setHot(microblog.getCHeat());

        if (microblog.getCImage() != null){
            List<String> stringList = Arrays.asList(microblog.getCImage().split(","));
            weibo.setImageList(stringList);
        }

        //返回微博和用户信息
        return result > 0 ? R.ok("发布成功").addData("weibo", weibo) : R.error("发布失败");
    }

    @Override
    public R deleteWeibo(Long uId, Long cId) {

        //删除数据库的微博
        int result = microblogMapper.deleteById(cId);

        //删除缓存的微博
        redisTemplate.delete("weibo:" + cId);

        //删除缓存微博的id
        redisTemplate.opsForList().remove("WeiboID:", 0, cId);

        //更新缓存信息，用户发布的微博数减一
        UserDetail userDetail = (UserDetail) redisTemplate.opsForValue().get("UserDetail:" + uId);
        if (userDetail != null){
            userDetail.setArticle(userDetail.getArticle() - 1);
            redisTemplate.opsForValue().set("UserDetail:" + uId, userDetail);
        }

        return result > 0 ? R.ok("删除成功") : R.error("删除失败");
    }

    @Override
    public R findAllWeibo(Long uId, Long page, Long limit) {

        //创建分页对象  注意加参数
        Page<Microblog> pageParam = new Page<>(page,limit);

        //执行业务方法，给分页对象赋值
        microblogMapper.selectPage(pageParam, new QueryWrapper<Microblog>().orderByDesc("c_time"));

        //获取分页数据
        List<Microblog> microblogList = pageParam.getRecords();

        if (microblogList.size() == 0){
            return R.error("没有数据了");
        }

        //存放数据,前端显示
        List<Weibo> weiboList = new ArrayList<>();


        List<UserAttentionMongo> objectList = new ArrayList<>();

        //用户已登录
        if (uId != 0L){
            //查询用户的所有关注
            objectList = userClient.findAllMyAttention(uId);

        }

        //遍历查询出来的微博
        for (Microblog microblog : microblogList) {

            Weibo weibo = new Weibo();

            //从缓存获取当前用户的信息
            UserDetail userDetail = (UserDetail) redisTemplate.opsForValue().get("UserDetail:" + microblog.getUId());

            if (uId != 0){

                //查询mongodb微博的点赞数组是否有当前登录用户
                MicroblogPojo object = microblogComponent.selectList(microblog.getId(), uId, "likeList");

                //如果存在，则当前用户已点赞该微博
                if (object != null){
                    weibo.setCode(1);
                }

                //查看当前微博是不是自己发布的
                if (!microblog.getUId().equals(uId)){

                    //对该用户的关注状态先置0，也就是没有关注，然后再去遍历看是否有关注
                    weibo.setAttention(0);

                    //判断关注数组是否有数据
                    if (objectList != null && objectList.size() > 0){

                        //遍历自己的关注数组是否关注了该用户
                        for (UserAttentionMongo jsonObject : objectList) {

                            //如果自己关注了该用户
                            if (microblog.getUId().equals(jsonObject.getU2Id())){
                                weibo.setAttention(1);
                            }

                        }

                    }

                }
            }

            //设置weibo实体参数
            assert userDetail != null;
            weibo.setId(microblog.getId())
                    .setNickname(userDetail.getNickname())
                    .setVip(userDetail.getVip())
                    .setMicroblog(microblog)
                    .setTime(DateUtil.dateTimeMM(microblog.getCTime()))
                    .setHot(microblog.getCHeat());
            if (userDetail.getAvatar() != null){
                weibo.setAvatar(userDetail.getAvatar());
            }

            if (microblog.getCImage() != null){
                List<String> stringList = Arrays.asList(microblog.getCImage().split(","));
                weibo.setImageList(stringList);
            }
            // 添加到数组里
            weiboList.add(weibo);
        }

        //按照微博热度排序
        //weiboList.sort(Comparator.comparing(Weibo::getHot).reversed());

        //按照微博发布时间排序
        //weiboList.sort(Comparator.comparing(Weibo::getTime).reversed());

        return R.ok("查询成功").addData("weiboList", weiboList);
    }

    @Override
    public R findWeiboById(Long cId) {
        Microblog microblog = (Microblog) redisTemplate.opsForValue().get("weibo:" + cId);

        //从缓存获取用户信息
        assert microblog != null;
        UserDetail userDetail = (UserDetail) redisTemplate.opsForValue().get("UserDetail:" + microblog.getUId());
        Weibo weibo = new Weibo();

        assert userDetail != null;
        weibo.setId(microblog.getId()).setMicroblog(microblog)
                .setNickname(userDetail.getNickname())
                .setVip(userDetail.getVip())
                .setAvatar(userDetail.getAvatar())
                .setTime(DateUtil.dateTimeMM(microblog.getCTime()))
                .setHot(microblog.getCHeat());

        //如果有图片
        if (microblog.getCImage() != null){
            List<String> stringList = Arrays.asList(microblog.getCImage().split(","));
            weibo.setImageList(stringList);
        }

        //获取评论(按时间排序)
        List<CommentMongo_1> list = commentClient.selectAllComment(microblog.getId(), 0);

        //微博热度+100
        int a = microblog.getCHeat();
        microblog.setCHeat(a + 100);

        //更新到缓存
        redisTemplate.opsForValue().set("weibo:" + microblog.getId(), microblog);

        return R.ok("查询成功").addData("weibo", weibo).addData("commentList", list);
    }

    @Override
    public List<Weibo> findAllWeiboVideo() {
        //缓存中获取所有微博id
        List<Object> weiboId = null;
        weiboId = redisTemplate.opsForList().range("WeiboID:", 0, -1);

        //存放微博的数组
        List<Microblog> microblogList = new ArrayList<>();


        if (weiboId == null){
            //如果缓存中没有微博id，则去数据库找
            microblogList =  microblogMapper.selectList(null);
            if (microblogList == null){
                return null;
            }

        }else{

            //遍历微博id去缓存获取微博详细信息，并存到数组里
            for (Object o : weiboId) {
                Microblog microblog = (Microblog) redisTemplate.opsForValue().get("weibo:" + o);
                microblogList.add(microblog);
            }
        }
        //如果没有人发布视频
        if (microblogList.size() < 1){
            return null;
        }

        //存放数据,前端显示
        List<Weibo> weiboList = new ArrayList<>();

        Weibo weibo= null;
        UserDetail userDetail = null;

        //遍历数据库查出来的数组
        for (Microblog microblog : microblogList) {
            if (microblog.getCVideo() != null){

                weibo = new Weibo();
                //从缓存获取当前用户的信息
                userDetail = (UserDetail) redisTemplate.opsForValue().get("UserDetail:" + microblog.getUId());

                //设置weibo实体参数
                assert userDetail != null;
                weibo.setId(microblog.getId())
                        .setAvatar(userDetail.getAvatar())
                        .setNickname(userDetail.getNickname())
                        .setVip(userDetail.getVip())
                        .setMicroblog(microblog)
                        .setTime(DateUtil.dateTimeMM(microblog.getCTime()))
                        .setHot(microblog.getCHeat());
                // 添加到数组里
                weiboList.add(weibo);
            }
        }
        return weiboList;
    }

    @Override
    public R updateWeiboInfo(Microblog microblog) {
        UpdateWrapper<Microblog> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", microblog.getId());
        int result = microblogMapper.update(microblog, updateWrapper);
        return result > 0 ? R.ok("更新成功") : R.error("更新失败");
    }


    @Override
    public List<Microblog> SearchInDbMicroblog(List<Long> needSearchCId) {
        return microblogMapper.selectBatchIds(needSearchCId);
    }

    @Override
    public List<Microblog> selectByKeyword(String keyword) {
        return microblogMapper.selectByKeyword(keyword);
    }


}
