package com.demo.weibo.microblog.controller;

import com.demo.weibo.common.util.R;
import com.demo.weibo.microblog.service.MicroblogOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/operation")
public class MicroblogOperationController {

    @Autowired
    private MicroblogOperationService microblogOperationService;

    /**
     * 点赞微博
     * @param request
     * @param cId
     * @return
     */
    @GetMapping("/like")
    public R addLikeMicroblog(HttpServletRequest request, @RequestParam("uid") Long uId, @RequestParam("cid") Long cId){
        return microblogOperationService.addLikeWeibo(uId, cId);
    }

    /**
     * 取消点赞微博
     * @param request
     * @param uId
     * @param cId
     * @return
     */
    @GetMapping("/unlike")
    public R cancelLikeMicroblog(HttpServletRequest request, @RequestParam("uid") Long uId, @RequestParam("cid") Long cId){
        return microblogOperationService.cancelLikeWeibo(uId, cId);
    }

}
