package com.demo.weibo.microblog.service;

import com.demo.weibo.common.entity.Microblog;
import com.demo.weibo.common.util.R;

public interface MicroblogService {

    R releaseWeiBo(Long uId, Microblog microblog);

    R deleteWeibo(Long uId, Long cId);
}
