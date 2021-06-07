package com.demo.weibo.file.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    List<String> uploadImageFile(MultipartFile[] files);

    String uploadVideoFile(MultipartFile file);

}
