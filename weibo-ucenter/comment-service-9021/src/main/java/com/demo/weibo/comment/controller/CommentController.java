package com.demo.weibo.comment.controller;

import com.demo.weibo.comment.service.CommentService;
import com.demo.weibo.common.annotation.UserLoginAnnotation;
import com.demo.weibo.common.entity.Microblog;
import com.demo.weibo.common.entity.User;
import com.demo.weibo.common.entity.mongo.CommentMongo_1;
import com.demo.weibo.common.util.DateUtil;
import com.demo.weibo.common.util.R;
import com.demo.weibo.common.util.id.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 添加一级评论
     * @param request   获取当前用户
     * @param param 评论的信息
     * @return  R
     */
    @PostMapping("/add")
    @UserLoginAnnotation
    public R addComment(HttpServletRequest request, @RequestBody Map<String, Object> param){
        User user = (User) request.getAttribute("weiboUser");
        param.put("uId", user.getId());
        return commentService.addComment(param);
    }

    /**
     * 删除一级评论包括其回复
     * @param param 微博id和评论id
     * @return  R
     */
    @GetMapping("/remove")
    @UserLoginAnnotation
    public R removeComment( @RequestBody Map<String, Object> param){
        return commentService.removeComment(param);
    }

    /**
     * 查询当前微博所有评论
     * @param cId   微博id
     * @return  List
     */
    @GetMapping("/all")
    public R selectAllComment( @RequestParam("cid") Long cId,@RequestParam("strategy") Integer strategy){
        return commentService.selectAllComment(cId, strategy);
    }


}
