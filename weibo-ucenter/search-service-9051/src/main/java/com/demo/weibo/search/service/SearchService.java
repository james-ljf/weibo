package com.demo.weibo.search.service;

import com.demo.weibo.common.util.R;

public interface SearchService {

    R searchByKeyword(Long userId, String keyword, int times);

}
