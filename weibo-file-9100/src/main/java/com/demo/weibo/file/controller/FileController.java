package com.demo.weibo.file.controller;

import com.demo.weibo.file.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/file/upload")
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * 上传图片
     * @param files 文件数组
     * @return  String
     */
    @PostMapping("/image")
    public List<String> uploadImage(@RequestBody List<MultipartFile> files){
        return fileService.uploadImageFile(files);
    }

    /**
     * 上传单个图片(头像、微博图片)
     * @param file  单个文件
     * @return  String
     */
    @PostMapping("/one-file")
    public String uploadOneFile(MultipartFile file){
        return fileService.uploadOneFile(file);
    }

    /**
     * 上传视频
     * @param file  视频文件
     * @return  String
     */
    @PostMapping("/video")
    public String uploadVideo(MultipartFile file){
        return fileService.uploadVideo(file);
    }

}
