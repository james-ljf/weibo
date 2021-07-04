package com.demo.weibo.microblog.controller;

import com.demo.weibo.api.client.FileClient;
import com.demo.weibo.common.annotation.UserLoginAnnotation;
import com.demo.weibo.common.entity.Microblog;
import com.demo.weibo.common.entity.User;
import com.demo.weibo.common.entity.msg.Weibo;
import com.demo.weibo.common.util.R;
import com.demo.weibo.common.util.file.MediaTypeUtils;
import com.demo.weibo.common.util.id.IdGenerator;
import com.demo.weibo.microblog.service.MicroblogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/microblog")
public class MicroblogController {

    @Autowired
    private FileClient fileClient;

    @Autowired
    private MicroblogService microblogService;

    /**
     * 发布微博
     * @param request 当前用户id
     * @param cContent 微博内容
     * @return R
     */
    @PostMapping("/release")
    @UserLoginAnnotation
    public R releaseMicroblog(HttpServletRequest request,
                              @RequestParam("c_content") String cContent,
                              @RequestParam("image") List<String> imageList,
                              @RequestParam("video") String video){
        User user = (User) request.getAttribute("weiboUser");
        Microblog microblog = new Microblog();

        //如果有视频
        if (video != null){
            microblog.setCVideo(video);
        }

        if (imageList.size() > 0){
            microblog.setCImage(imageList);
        }
        microblog.setCContent(cContent);
        return microblogService.releaseWeiBo(user.getId(), microblog);
    }

    /**
     * 删除微博
     //* @param request 获取当前用户id
     * @param cId   微博id
     * @return R
     */
    @GetMapping("/delete")
    public R deleteMicroblog(HttpServletRequest request,
                             @RequestParam("cid") Long cId){
        User user = (User) request.getAttribute("weiboUser");
        return microblogService.deleteWeibo(user.getId(), cId);
    }

    /**
     * 查询所有微博 (未登录)
     * @return List
     */
    @PostMapping("/all")
    public List<Weibo> findAllMicroblog(){
        List<Weibo> weiboList = new ArrayList<>();
        weiboList = microblogService.findAllWeibo(0L);
        return weiboList;
    }

    /**
     * 查询所有微博（已登录）
     */
    @PostMapping("/all-weibo")
    @UserLoginAnnotation
    public List<Weibo> findAllWeibo(HttpServletRequest request){
        User user = (User) request.getAttribute("weiboUser");
        List<Weibo> weiboList = new ArrayList<>();
        weiboList = microblogService.findAllWeibo(user.getId());
        return weiboList;
    }

    /**
     * 根据微博id查询微博内容
     * @param cId   微博id
     * @return  R
     */
    @GetMapping("/find/{cId}")
    public R findMicroblogById(@PathVariable Long cId){
        return microblogService.findWeiboById(cId);
    }

    /**
     * 查询所有发布了视频的微博
     */
    @PostMapping("/video-all")
    public List<Weibo> findAllMicroblogVideo(){
        return microblogService.findAllWeiboVideo();
    }

    /**
     * 根据 id 的链表查询所有微博
     */
    @PostMapping("/in-db")
    public List<Microblog> SearchInDbMicroblog(@RequestParam("needSearchCId") List<Long> needSearchCId){
        return microblogService.SearchInDbMicroblog(needSearchCId);
    }

    /**
     * 关键词查询微博
     * @param keyword   关键词
     * @return List
     */
    @PostMapping("/search-keyword")
    public List<Microblog> selectByKeyword(@RequestParam("keyword") String keyword){
        return null;
    }


}
