package com.demo.weibo.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(value = "file-service")
public interface FileClient {

    @PostMapping("/file/upload/image")
    List<String> uploadImage(@RequestParam("files") List<MultipartFile> files);

    @PostMapping("/file/upload/one-file")
    String uploadOneFile(@RequestParam("file") MultipartFile file);

    @PostMapping("/file/upload/video")
    String uploadVideo(@RequestParam("file") MultipartFile file);
}
