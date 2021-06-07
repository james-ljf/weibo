package com.demo.weibo.file.controller;

import com.demo.weibo.common.util.R;
import com.demo.weibo.file.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/file/upload")
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * 上传图片去fast
     * @param files
     * @return
     */
    @PostMapping("/image")
    public List<String> uploadImage(@RequestParam("files") MultipartFile[] files){
        return fileService.uploadImageFile(files);
    }

    @PostMapping("/video")
    public String uploadVideo(@RequestParam("file") MultipartFile file){
        return fileService.uploadVideoFile(file);
    }

}
