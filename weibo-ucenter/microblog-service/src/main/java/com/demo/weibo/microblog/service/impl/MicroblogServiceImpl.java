package com.demo.weibo.microblog.service.impl;

import com.demo.weibo.common.entity.Microblog;
import com.demo.weibo.common.entity.UserDetail;
import com.demo.weibo.common.filter.RedissonBloomFilter;
import com.demo.weibo.common.util.R;
import com.demo.weibo.common.util.id.IdGenerator;
import com.demo.weibo.microblog.mapper.MicroblogMapper;
import com.demo.weibo.microblog.mapper.MicroblogRepository;
import com.demo.weibo.microblog.service.MicroblogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class MicroblogServiceImpl implements MicroblogService {

    @Autowired
    private MicroblogMapper microblogMapper;

    @Autowired
    private MicroblogRepository microblogRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public R releaseWeiBo(Long uId, Microblog microblog) {
        //雪花算法生成微博id
        microblog.setId(IdGenerator.snowflakeId()).setUId(uId).setCTime(new Date());
        int result = microblogMapper.insert(microblog);
        //存进elasticsearch，方便搜索
        System.out.println(microblogRepository.save(microblog));
        //更新缓存信息，用户发布的微博数加一
        UserDetail userDetail = (UserDetail) redisTemplate.opsForValue().get("UserDetail:" + uId);
        if (userDetail != null){
            userDetail.setArticle(userDetail.getArticle() + 1);
            redisTemplate.opsForValue().set("UserDetail:" + uId, userDetail);
        }
        return result > 0 ? R.ok("发布成功") : R.error("发布失败");
    }

    @Override
    public R deleteWeibo(Long uId, Long cId) {
        int result = microblogMapper.deleteById(cId);
        //删除在es引擎的微博
        microblogRepository.deleteById(cId);
        //更新缓存信息，用户发布的微博数减一
        UserDetail userDetail = (UserDetail) redisTemplate.opsForValue().get("UserDetail:" + uId);
        if (userDetail != null){
            userDetail.setArticle(userDetail.getArticle() - 1);
            redisTemplate.opsForValue().set("UserDetail:" + uId, userDetail);
        }
        return result > 0 ? R.ok("删除成功") : R.error("删除失败");
    }


}
