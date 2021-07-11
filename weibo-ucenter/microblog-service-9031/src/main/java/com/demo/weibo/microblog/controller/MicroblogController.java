package com.demo.weibo.microblog.controller;

import com.demo.weibo.common.annotation.UserLoginAnnotation;
import com.demo.weibo.common.entity.Microblog;
import com.demo.weibo.common.entity.User;
import com.demo.weibo.common.entity.msg.ReleaseData;
import com.demo.weibo.common.entity.msg.Weibo;
import com.demo.weibo.common.util.R;
import com.demo.weibo.microblog.service.MicroblogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/microblog")
public class MicroblogController {

    @Autowired
    private MicroblogService microblogService;

    /**
     * 发布微博
     * @param request 当前用户id
     * @param releaseData 微博内容
     * @return R
     */
    @PostMapping("/release")
    @UserLoginAnnotation
    public R releaseMicroblog(HttpServletRequest request,
                              @RequestBody ReleaseData releaseData){
        User user = (User) request.getAttribute("weiboUser");
        Microblog microblog = new Microblog();

        String video = releaseData.getVideo();
        List<String> imageList = releaseData.getImage();
        String cContent = releaseData.getContent();

        //如果有视频
        if (video != null){
            microblog.setCVideo(video);
        }

        if (imageList.size() > 0){
            String res = "";
            for (String s : imageList) {
                res = res + s + ",";
            }
            microblog.setCImage(res);
        }
        microblog.setCContent(cContent);
        return microblogService.releaseWeiBo(user.getId(), microblog);
    }

    /**
     * 删除微博
     * @param request 获取当前用户id
     * @param cId   微博id
     * @return R
     */
    @GetMapping("/delete")
    @UserLoginAnnotation
    public R deleteMicroblog(HttpServletRequest request,
                             @RequestParam("cid") Long cId){
        User user = (User) request.getAttribute("weiboUser");
        return microblogService.deleteWeibo(user.getId(), cId);
    }

    /**
     * 分页查询所有微博 (未登录)
     * @return List
     */
    @PostMapping("/all/{page}/{limit}")
    public R findAllMicroblog(@PathVariable Long page,
                                        @PathVariable Long limit){

        return microblogService.findAllWeibo(0L, page, limit);
    }

    /**
     * 查分页询所有微博（已登录）
     */
    @PostMapping("/all-weibo/{page}/{limit}")
    @UserLoginAnnotation
    public R findAllWeibo(HttpServletRequest request,
                                    @PathVariable Long page,
                                    @PathVariable Long limit){
        User user = (User) request.getAttribute("weiboUser");
        return microblogService.findAllWeibo(user.getId(), page, limit);
    }

    /**
     * 查询所有微博显示在个人主页
     * @param request   获取当前用户
     * @return  R
     */
    @PostMapping("/my-all/weibo")
    @UserLoginAnnotation
    public R findAllMyWeibo(HttpServletRequest request){
        User user = (User) request.getAttribute("weiboUser");
        return microblogService.findAllMyWeibo(user.getId());
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
     * 根据用户id查询发布的所有微博(未登录)
     * @param uId   用户id
     * @return  R
     */
    @GetMapping("/find-by-uId/{uId}")
    public R findAllMicroblogByUId(@PathVariable Long uId){
        return microblogService.findAllWeiboByUId(0L, uId);
    }

    /**
     * 根据用户id查询发布的所有微博(已登录)
     * @param request   获取当前登录用户
     * @param uId   要查询的用户id
     * @return  R
     */
    @GetMapping("/find-by-uId/login/{uId}")
    @UserLoginAnnotation
    public R findAllMicroblogByUId(HttpServletRequest request, @PathVariable Long uId){
        User user = (User) request.getAttribute("weiboUser");
        return microblogService.findAllWeiboByUId(user.getId(), uId);
    }

    /**
     * 查询所有发布了视频的微博
     */
    @PostMapping("/video-all")
    public List<Weibo> findAllMicroblogVideo(){
        return microblogService.findAllWeiboVideo();
    }

    /**
     * 更新微博信息(服务调用)
     * @param microblog 微博
     * @return  R
     */
    @PostMapping("/update")
    public R updateWeiboInfo(@RequestBody Microblog microblog){
        return microblogService.updateWeiboInfo(microblog);
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
