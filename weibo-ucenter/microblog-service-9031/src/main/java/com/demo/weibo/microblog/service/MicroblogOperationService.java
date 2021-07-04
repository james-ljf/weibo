package com.demo.weibo.microblog.service;

import com.demo.weibo.common.util.R;

public interface MicroblogOperationService {

    R addLikeWeibo(Long uId, Long cId);

    R cancelLikeWeibo(Long uId, Long cId);

    boolean isLikeMicroblog(Long cId, Long uId);

}
