package com.demo.weibo.microblog.service;

import com.demo.weibo.common.entity.Microblog;
import com.demo.weibo.common.entity.msg.Weibo;
import com.demo.weibo.common.util.R;

import java.util.List;

public interface MicroblogService {

    R releaseWeiBo(Long uId, Microblog microblog);

    R deleteWeibo(Long uId, Long cId);

    List<Weibo> findAllWeibo(Long uId);

    R findWeiboById(Long cId);

    List<Weibo> findAllWeiboVideo();

    List<Microblog> SearchInDbMicroblog(List<Long> needSearchCId);

    List<Microblog> selectByKeyword(String keyword);

}
