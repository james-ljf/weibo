package com.demo.weibo.api.client;

import com.demo.weibo.common.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(value = "weibo-file")
public interface FileClient {

    @PostMapping("/file/upload/image")
    List<String> uploadImage(MultipartFile[] files);

    @PostMapping("/file/upload/video")
    String uploadVideo(MultipartFile file);

}
