package com.demo.weibo.api.client;

import com.demo.weibo.common.entity.Microblog;
import com.demo.weibo.common.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("microblog-service")
public interface MicroblogClient {

    @PostMapping("/api/operation/is-like")
    boolean isLikeMicroblog(@RequestParam("cId") Long cId, @RequestParam("uId") Long uId);

    @PostMapping("api/microblog/update")
    R updateWeiboInfo(@RequestBody Microblog microblog);

    @PostMapping("/api/microblog/in-db")
    List<Microblog> SearchInDbMicroblog(@RequestParam("needSearchCId") List<Long> needSearchCId);

    @PostMapping("/api/microblog/search-keyword")
    List<Microblog> selectByKeyword(@RequestParam("keyword") String keyword);

}
