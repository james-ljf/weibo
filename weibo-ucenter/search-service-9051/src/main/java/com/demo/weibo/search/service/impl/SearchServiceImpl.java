package com.demo.weibo.search.service.impl;

import com.demo.weibo.api.client.MicroblogClient;
import com.demo.weibo.common.entity.Microblog;
import com.demo.weibo.common.entity.UserDetail;
import com.demo.weibo.common.entity.msg.Weibo;
import com.demo.weibo.common.util.R;
import com.demo.weibo.common.util.RedisLockUtil;
import com.demo.weibo.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 搜索业务
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private MicroblogClient microblogClient;

    @Override
    public R searchByKeyword(Long userId, String keyword, int times) {
        if (times > 2){
            return R.error("服务器太繁忙了，请稍后再试！");
        }

        //查询出的微博
        List<Weibo> weiboList = null;

        //查询出的用户
        List<UserDetail> userDetailList = null;

        //缓存里微博id的链表
        List<Object> redisWeiboIdList = null;

        //缓存里用户id的链表
        List<Object> redisUserDetailIdList = null;

        //还不存在的微博Id
        List<Long> needSearchCId = null;

        //还不存在的用户id
        List<Long> needSearchUId = null;

        //查询缓存
        redisWeiboIdList = redisTemplate.opsForList().range("weiboSearch:" + keyword, 0, -1);
        redisUserDetailIdList = redisTemplate.opsForList().range("userDetailSearch:" + keyword, 0, -1);

        if (redisWeiboIdList != null && redisUserDetailIdList != null
                && redisUserDetailIdList.size() > 0 && redisWeiboIdList.size() > 0){
            //当有缓存

            weiboList = new ArrayList<>();
            userDetailList = new ArrayList<>();
            needSearchCId = new ArrayList<>();

            //遍历存储微博id的链表
            for (Object cId : redisWeiboIdList) {

                //获取缓存中的微博
                Microblog microblog = (Microblog) redisTemplate.opsForValue().get("weibo:" + cId);
                if (microblog == null) {
                    //该微博不存在缓存，则将微博id加入数组
                    needSearchCId.add((Long) cId);

                }else {

                    //微博存在缓存
                    //获取用户信息
                    UserDetail userDetail = (UserDetail) redisTemplate.opsForValue().get("UserDetail:" + microblog.getUId());
                    Weibo weibo = new Weibo();
                    assert userDetail != null;
                    weibo.setMicroblog(microblog).setId(microblog.getId())
                            .setAvatar(userDetail.getAvatar())
                            .setNickname(userDetail.getNickname())
                            .setHot(microblog.getCHeat());

                    //如果已登录
                    if (userId != null){
                        //查询当前用户是否点赞了该微博
                        boolean is = microblogClient.isLikeMicroblog(microblog.getId(), userId);

                        if (is){
                            //如果已点赞，置1
                            weibo.setCode(1);
                        }
                    }
                    weiboList.add(weibo);

                }
            }

            //如果不存在缓存的微博id的链表长度大于0
            if (needSearchCId.size() > 0 ){
                //加锁
                RedisLockUtil.lock(stringRedisTemplate, keyword);

                List<Microblog> microblogInDb = microblogClient.SearchInDbMicroblog(needSearchCId);

                //释放锁
                RedisLockUtil.unlock(stringRedisTemplate, keyword);

                if (microblogInDb != null && microblogInDb.size() > 0){
                    for (Microblog microblog : microblogInDb) {
                        //将结果存入redis
                        redisTemplate.opsForValue().set("weibo:" + microblog.getId(), microblog, (int)(Math.random()*(31) + 100), TimeUnit.MINUTES);

                        Weibo weibo = new Weibo();
                        //缓存获取用户信息
                        UserDetail userDetail = (UserDetail) redisTemplate.opsForValue().get("UserDetail:" + microblog.getUId());
                        assert userDetail != null;
                        weibo.setMicroblog(microblog).setId(microblog.getId())
                                .setAvatar(userDetail.getAvatar())
                                .setNickname(userDetail.getNickname())
                                .setHot(microblog.getCHeat());

                        //如果已登录
                        if (userId != null){
                            //查询当前用户是否点赞了该微博
                            boolean is = microblogClient.isLikeMicroblog(microblog.getId(), userId);

                            if (is){
                                //如果已点赞，置1
                                weibo.setCode(1);
                            }
                        }
                        weiboList.add(weibo);
                    }

                }

            }

        }else{ //缓存不存在

            weiboList = new ArrayList<>();

            //查询数据库
            if (RedisLockUtil.lock(stringRedisTemplate, keyword)){
                //获得锁
                List<Microblog> microblogList = microblogClient.selectByKeyword(keyword);
                if (microblogList != null){
                    for (Microblog microblog : microblogList) {
                        //将结果存入缓存
                        redisTemplate.opsForList().leftPush("weiboSearch:" + keyword, microblog.getId());
                        redisTemplate.opsForValue().set("weibo:" + microblog.getId(), microblog, (int)(Math.random()*(31) + 120), TimeUnit.MINUTES);

                        Weibo weibo = new Weibo();
                        //缓存获取用户信息
                        UserDetail userDetail = (UserDetail) redisTemplate.opsForValue().get("UserDetail:" + microblog.getUId());
                        assert userDetail != null;
                        weibo.setMicroblog(microblog).setId(microblog.getId())
                                .setAvatar(userDetail.getAvatar())
                                .setNickname(userDetail.getNickname())
                                .setHot(microblog.getCHeat());

                        //如果已登录
                        if (userId != null){
                            //查询当前用户是否点赞了该微博
                            boolean is = microblogClient.isLikeMicroblog(microblog.getId(), userId);

                            if (is){
                                //如果已点赞，置1
                                weibo.setCode(1);
                            }
                        }
                        weiboList.add(weibo);
                    }
                    redisTemplate.expire("weiboSearch:" + keyword, (int)(Math.random()*(51) + 100), TimeUnit.MINUTES);
                }
                RedisLockUtil.unlock(stringRedisTemplate, keyword);
            }else { //没有获得锁
                try {
                    TimeUnit.MILLISECONDS.sleep(200);
                    //再查一次
                    return searchByKeyword(userId, keyword, times);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        //按照微博热度排序
        weiboList.sort(Comparator.comparing(Weibo::getHot).reversed());

        return R.ok("查询成功").addData("weiboList", weiboList);
    }

}
