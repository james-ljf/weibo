package com.demo.weibo.microblog.service;

import com.demo.weibo.common.entity.Microblog;
import com.demo.weibo.common.entity.msg.Weibo;
import com.demo.weibo.common.util.R;

import java.util.List;

public interface MicroblogService {

    R releaseWeiBo(Long uId, Microblog microblog);

    R deleteWeibo(Long uId, Long cId);

    R findAllWeibo(Long uId, Long page, Long limit);

    R findWeiboById(Long cId);

    List<Weibo> findAllWeiboVideo();

    R updateWeiboInfo(Microblog microblog);

    List<Microblog> SearchInDbMicroblog(List<Long> needSearchCId);

    List<Microblog> selectByKeyword(String keyword);

}
