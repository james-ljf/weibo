package com.demo.weibo.microblog.controller;

import com.demo.weibo.api.client.FileClient;
import com.demo.weibo.common.annotation.UserLoginAnnotation;
import com.demo.weibo.common.entity.Microblog;
import com.demo.weibo.common.entity.User;
import com.demo.weibo.common.util.R;
import com.demo.weibo.common.util.file.MediaTypeUtils;
import com.demo.weibo.microblog.service.MicroblogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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
     * @param microblog 微博实体类数据
     * @param file  文件（图片、视频）
     * @return R
     */
    @PostMapping("/release")
    @UserLoginAnnotation
    public R releaseMicroblog(HttpServletRequest request,
                              @RequestBody Microblog microblog,
                              @RequestParam(value = "file",required=false) MultipartFile[] file){
        User user = (User) request.getAttribute("weiboUser");
        if (file.length == 1){
            //上传的是视频
            String videoFile = fileClient.uploadOneFile(file[0]);
            microblog.setCVideo(videoFile);
        }else {
            //判断是否都是图片类型
            String state = MediaTypeUtils.getImage(file);
            if (state.equals("0")){
                return R.error("您的上传类型不统一，请重新上传。");
            }
            //上传的是图片
            List<String> fileList = fileClient.uploadImage(file);
            microblog.setCImage(fileList);
        }

        return microblogService.releaseWeiBo(user.getId(), microblog);
    }

    /**
     * 删除微博
     * @param request 获取当前用户id
     * @param cId   微博id
     * @return R
     */
    @GetMapping("/delete")
    public R deleteMicroblog(HttpServletRequest request,
                             @RequestParam("uid") Long uId,
                             @RequestParam("cid") Long cId){
        User user = (User) request.getAttribute("weiboUser");
        return microblogService.deleteWeibo(uId, cId);
    }

}
