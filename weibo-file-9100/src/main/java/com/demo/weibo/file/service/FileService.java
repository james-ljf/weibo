package com.demo.weibo.file.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    List<String> uploadImageFile(MultipartFile[] files);

    String uploadOneFile(MultipartFile file);

    String uploadVideo(MultipartFile file);

}
