package com.demo.weibo.api.client;

import com.demo.weibo.common.entity.mongo.CommentMongo_1;
import com.demo.weibo.common.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "comment-service")
public interface CommentClient {

    @GetMapping("/comment/all")
    List<CommentMongo_1> selectAllComment(@RequestParam("cid") Long cId, Integer strategy);

}
