package com.demo.weibo.microblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.weibo.common.entity.Microblog;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MicroblogMapper extends BaseMapper<Microblog> {

    List<Microblog> selectByKeyword(@Param("keyword") String keyword);

}
