package com.demo.weibo.microblog.mapper;

import com.demo.weibo.common.entity.Microblog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

public interface MicroblogRepository extends ElasticsearchRepository<Microblog, Long> {
}
