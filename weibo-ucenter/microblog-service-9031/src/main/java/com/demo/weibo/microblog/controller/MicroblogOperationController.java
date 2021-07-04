package com.demo.weibo.microblog.controller;

import com.demo.weibo.common.entity.User;
import com.demo.weibo.common.util.R;
import com.demo.weibo.microblog.service.MicroblogOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/operation")
public class MicroblogOperationController {

    @Autowired
    private MicroblogOperationService microblogOperationService;

    /**
     * 点赞微博
     * @param request   获取当前用户
     * @param cId   微博id
     * @return  R
     */
    @GetMapping("/like")
    public R addLikeMicroblog(HttpServletRequest request, @RequestParam("uid") Long uId, @RequestParam("cid") Long cId){
        return microblogOperationService.addLikeWeibo(uId, cId);
    }

    /**
     * 取消点赞微博
     * @param request   获取当前用户
     * @param cId   微博id
     * @return  R
     */
    @GetMapping("/unlike")
    public R cancelLikeMicroblog(HttpServletRequest request,  @RequestParam("cid") Long cId){
        User user = (User) request.getAttribute("weiboUser");
        if (user == null){
            return R.error("出错了，你还没有登录");
        }
        return microblogOperationService.cancelLikeWeibo(user.getId(), cId);
    }

    /**
     * 查看用户是否点赞了该微博（服务调用）
     * @param cId   微博id
     * @param uId   用户id
     * @return  boolean
     */
    @PostMapping("/is-like")
    public boolean isLikeMicroblog(@RequestParam("cId") Long cId, @RequestParam("uId") Long uId){
        return microblogOperationService.isLikeMicroblog(cId, uId);
    }

}
