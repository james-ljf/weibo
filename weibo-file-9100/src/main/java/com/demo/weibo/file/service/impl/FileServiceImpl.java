package com.demo.weibo.file.service.impl;

import com.demo.weibo.file.service.FileService;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FastFileStorageClient fileStorageClient;

    @Override
    public List<String> uploadImageFile(MultipartFile[] files) {
        List<String> fileList = new ArrayList<>();
        int i = 0;
        for (MultipartFile file : files) {
            //获取文件名
            String fileName = file.getOriginalFilename();
            //获取后缀名
            String suffixName = null;
            if (fileName != null){
                suffixName = fileName.substring(fileName.lastIndexOf(".") + 1);
            }
            if (suffixName != null){
                try {
                    //上传文件
                    StorePath storePath = fileStorageClient.uploadFile(file.getInputStream(),
                            file.getSize(),
                            suffixName,
                            null);
                    System.out.println(storePath.getFullPath());
                    fileList.set(i, storePath.getFullPath());
                    i++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(fileList);
        return fileList;
    }

    @Override
    public String uploadOneFile(MultipartFile file) {
        //获取文件名
        String fileName = file.getOriginalFilename();
        //获取后缀名
        String suffixName = null;
        if (fileName != null){
            suffixName = fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        if (suffixName != null){
            try {
                StorePath storePath = fileStorageClient.uploadFile(file.getInputStream(),
                        file.getSize(),
                        suffixName,
                        null);
                System.out.println(storePath.getFullPath());
                return storePath.getFullPath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}
