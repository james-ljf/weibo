package com.demo.weibo.microblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.weibo.common.entity.Microblog;
import org.springframework.stereotype.Repository;

@Repository
public interface MicroblogMapper extends BaseMapper<Microblog> {
}
